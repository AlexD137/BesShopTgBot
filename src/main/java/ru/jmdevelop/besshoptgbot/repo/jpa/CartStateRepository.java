package ru.jmdevelop.besshoptgbot.repo.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.jmdevelop.besshoptgbot.models.entity.CartState;

import java.util.Optional;

public interface CartStateRepository extends JpaRepository<CartState, Long> {

    @Query("SELECT cs.currentPage FROM CartState cs WHERE cs.chatId = :chatId")
    Optional<Integer> findPageByChatId(Long chatId);

    @Modifying
    @Transactional
    @Query("UPDATE CartState cs SET cs.currentPage = :newPage WHERE cs.chatId = :chatId")
    void updatePage(Long chatId, int newPage);

    // Поиск по chatId (для exists проверок)
    Optional<CartState> findByChatId(Long chatId);

    // Создание или обновление состояния
    default void saveState(Long chatId, int currentPage) {
        findByChatId(chatId).ifPresentOrElse(
                state -> {
                    state.setCurrentPage(currentPage);
                    save(state);
                },
                () -> save(new CartState(chatId, currentPage))
        );
    }
}