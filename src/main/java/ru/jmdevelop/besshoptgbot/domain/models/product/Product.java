package ru.jmdevelop.besshoptgbot.domain.models.product;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class Product {
    private Long id;
    private String name;
    private Size size;
    private String color;
    private String type;
    private List<Photo> photo;
    private List<Video> video;
    private Rating rate;
    private Reviews reviews;
    private String description;
    private BigDecimal price;
    private Integer stockQuantity;
    private String category;
    private BigDecimal deliveryCost;
    private ProductMedia media;
    private ProductReviews reviews;
}