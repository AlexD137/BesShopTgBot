package ru.jmdevelop.besshoptgbot.models.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
public class CartState {
    @Id
    private Long chatId;
    private int currentPage = 0;

    public CartState(Long chatId, int currentPage) {
        this.chatId = chatId;
        this.currentPage = currentPage;
    }
}