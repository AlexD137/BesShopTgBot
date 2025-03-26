package ru.jmdevelop.besshoptgbot.repo;

import ru.jmdevelop.besshoptgbot.models.entity.Product;

import java.util.List;

public interface ProductRepository {

    Product findById(Integer productId);

    List<Product> findAllByCategoryName(String categoryName, int offset, int size);

}