package ru.jmdevelop.besshoptgbot.domain.models.product;

import lombok.Data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

@Data
public class ProductReviews {
    private Long productId;
    private List<Review> reviews = new ArrayList<>();

    public void addReview(Review review) {
        reviews.add(review);
    }

    public Double getAverageRating() {
        if (reviews.isEmpty()) return 0.0;

        return reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);
    }

    public long getReviewsCount() {
        return reviews.size();
    }

    public List<Review> getRecentReviews(int count) {
        return reviews.stream()
                .sorted(Comparator.comparing(Review::getCreatedAt).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }
}
