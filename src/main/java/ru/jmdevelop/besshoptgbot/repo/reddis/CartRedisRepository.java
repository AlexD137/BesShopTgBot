package ru.jmdevelop.besshoptgbot.repo.reddis;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import ru.jmdevelop.besshoptgbot.models.dom.CartItem;
import ru.jmdevelop.besshoptgbot.repo.CartRepository;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Repository
public class CartRedisRepository implements CartRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private final HashOperations<String, String, Object> hashOps;

    private static final String CART_KEY_PREFIX = "user:cart:";
    private static final String PAGE_NUMBER_KEY = "user:page:";

    public CartRedisRepository(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOps = redisTemplate.opsForHash();
    }

    @Override
    public void saveCartItem(Long chatId, CartItem cartItem) {
        String key = CART_KEY_PREFIX + chatId;
        hashOps.put(key, cartItem.getId().toString(), cartItem);
        redisTemplate.expire(key, 7, TimeUnit.DAYS);
    }

    @Override
    public void updateCartItem(Long chatId, CartItem cartItem) {
        saveCartItem(chatId, cartItem);
    }

    @Override
    public void deleteCartItem(Long chatId, Integer cartItemId) {
        hashOps.delete(CART_KEY_PREFIX + chatId, cartItemId.toString());
    }

    @Override
    public CartItem findCartItemByChatIdAndProductId(Long chatId, Integer productId) {
        List<CartItem> items = findAllCartItemsByChatId(chatId);
        return items.stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<CartItem> findAllCartItemsByChatId(Long chatId) {
        return hashOps.values(CART_KEY_PREFIX + chatId).stream()
                .map(obj -> (CartItem) obj)
                .toList();
    }

    @Override
    public void deleteAllCartItemsByChatId(Long chatId) {
        redisTemplate.delete(CART_KEY_PREFIX + chatId);
    }

    @Override
    public Integer findPageNumberByChatId(Long chatId) {
        return (Integer) redisTemplate.opsForValue().get(PAGE_NUMBER_KEY + chatId);
    }

    @Override
    public void updatePageNumberByChatId(Long chatId, Integer pageNumber) {
        redisTemplate.opsForValue().set(
                PAGE_NUMBER_KEY + chatId,
                pageNumber,
                7,
                TimeUnit.DAYS
        );
    }
}