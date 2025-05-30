package ru.jmdevelop.besshoptgbot.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.jmdevelop.besshoptgbot.models.dom.CartItem;
import ru.jmdevelop.besshoptgbot.models.dom.CartView;
import ru.jmdevelop.besshoptgbot.repo.CartRepository;
import ru.jmdevelop.besshoptgbot.repo.jpa.CartStateRepository;
import ru.jmdevelop.besshoptgbot.repo.jpa.ProductRepository;
import ru.jmdevelop.besshoptgbot.services.CartService;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepo;
    private final ProductRepository productRepo;
    private final CartStateRepository stateRepo;

    @Override
    @Transactional(readOnly = true)
    public CartView getCartView(Long chatId) {
        List<CartItem> items = cartRepo.findByChatIdWithProduct(chatId);
        if (items.isEmpty()) {
            return new CartView(null, 0, 0, 0);
        }

        int currentPosition = stateRepo.findPageByChatId(chatId).orElse(0);
        CartItem currentItem = items.get(normalizePosition(currentPosition, items.size()));

        return new CartView(
                currentItem,
                currentPosition + 1,
                items.size(),
                calculateTotal(items)
        );
    }

    @Override
    @Transactional
    public void removeItem(Long chatId) {
        CartItem item = getCurrentItem(chatId);
        cartRepo.delete(item);

        List<CartItem> remainingItems = cartRepo.findByChatIdWithProduct(chatId);
        if (!remainingItems.isEmpty()) {
            updatePosition(chatId, normalizePosition(
                    stateRepo.findPageByChatId(chatId).orElse(0),
                    remainingItems.size()
            ));
        }
    }

    @Override
    @Transactional
    public void increaseQuantity(Long chatId) {
        modifyQuantity(chatId, 1);
    }

    @Override
    @Transactional
    public void decreaseQuantity(Long chatId) {
        modifyQuantity(chatId, -1);
    }

    @Override
    @Transactional
    public void nextItem(Long chatId) {
        List<CartItem> items = cartRepo.findByChatIdWithProduct(chatId);
        if (items.size() > 1) {
            int newPosition = (getCurrentPosition(chatId) + 1) % items.size();
            updatePosition(chatId, newPosition);
        }
    }

    @Override
    @Transactional
    public void previousItem(Long chatId) {
        List<CartItem> items = cartRepo.findByChatIdWithProduct(chatId);
        if (items.size() > 1) {
            int newPosition = (getCurrentPosition(chatId) - 1 + items.size()) % items.size();
            updatePosition(chatId, newPosition);
        }
    }

    //=== Приватные вспомогательные методы ===//
    private void modifyQuantity(Long chatId, int delta) {
        CartItem item = getCurrentItem(chatId);
        int newQuantity = item.getQuantity() + delta;

        if (newQuantity < 1 || newQuantity > 50) {
            throw new CartException("Количество должно быть от 1 до 50");
        }

        item.setQuantity(newQuantity);
        cartRepo.save(item);
    }

    private CartItem getCurrentItem(Long chatId) {
        List<CartItem> items = cartRepo.findByChatIdWithProduct(chatId);
        if (items.isEmpty()) {
            throw new CartException("Корзина пуста");
        }
        return items.get(getCurrentPosition(chatId));
    }

    private int getCurrentPosition(Long chatId) {
        List<CartItem> items = cartRepo.findByChatIdWithProduct(chatId);
        int savedPosition = stateRepo.findPageByChatId(chatId).orElse(0);
        return normalizePosition(savedPosition, items.size());
    }

    private void updatePosition(Long chatId, int newPosition) {
        stateRepo.findByChatId(chatId).ifPresentOrElse(
                state -> {
                    state.setCurrentPage(newPosition);
                    stateRepo.save(state);
                },
                () -> stateRepo.save(new CartState(chatId, newPosition))
        );
    }

    private int normalizePosition(int position, int itemsSize) {
        return itemsSize == 0 ? 0 : Math.max(0, Math.min(position, itemsSize - 1));
    }

    private double calculateTotal(List<CartItem> items) {
        return items.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
    }
}