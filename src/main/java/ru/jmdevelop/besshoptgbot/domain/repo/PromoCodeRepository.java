package ru.jmdevelop.besshoptgbot.domain.repo;

import ru.jmdevelop.besshoptgbot.domain.models.product.PromoCode;

import java.util.List;
import java.util.Optional;

public interface PromoCodeRepository {
    Optional<PromoCode> findById(Long id);
    Optional<PromoCode> findByCode(String code);
    List<PromoCode> findAllActive();
    PromoCode save(PromoCode promoCode);

    // Бизнес-методы
    boolean isCodeValid(String code);
    void incrementUsageCount(String code);
}