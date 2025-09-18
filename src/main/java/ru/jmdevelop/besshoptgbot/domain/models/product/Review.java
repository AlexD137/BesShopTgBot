package ru.jmdevelop.besshoptgbot.domain.models.product;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Review {
    private Long id;
    private Long userId;
    private String authorName;
    private Integer rating; // 1-5
    private String comment;
    private LocalDateTime createdAt;
    private boolean verifiedPurchase;
    private boolean helpful;

    public boolean isPositive() {
        return rating >= 4;
    }
}
