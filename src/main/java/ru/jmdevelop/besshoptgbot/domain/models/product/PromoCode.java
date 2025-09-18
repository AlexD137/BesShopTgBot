package ru.jmdevelop.besshoptgbot.domain.models.product;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PromoCode {
    private Long id;
    private String code;
    private DiscountType type;
    private BigDecimal value;
    private LocalDateTime validFrom;
    private LocalDateTime validTo;
    private Integer usageLimit;
    private Integer usedCount;
    private boolean isActive;

    public enum DiscountType {
        PERCENTAGE, FIXED_AMOUNT, FREE_SHIPPING
    }

    public boolean isValid() {
        return isActive &&
                LocalDateTime.now().isBefore(validTo) &&
                (usageLimit == null || usedCount < usageLimit);
    }
}
