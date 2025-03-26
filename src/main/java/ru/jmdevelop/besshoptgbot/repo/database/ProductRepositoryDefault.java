package ru.jmdevelop.besshoptgbot.repo.database;

import ru.jmdevelop.besshoptgbot.models.entity.Product;
import ru.jmdevelop.besshoptgbot.repo.ProductRepository;

import java.util.List;

import static ru.jmdevelop.besshoptgbot.repo.hibernate.HibernateTransactionFactory.inTransaction;

public class ProductRepositoryDefault implements ProductRepository {

    @Override
    public Product findById(Integer productId) {
        return inTransaction(session -> session.get(Product.class, productId));
    }

    @Override
    public List<Product> findAllByCategoryName(String categoryName, int offset, int size) {
        String query = "from Product where category.name = :categoryName";

        return inTransaction(session ->
                session.createQuery(query, Product.class)
                        .setParameter("categoryName", categoryName)
                        .setFirstResult(offset)
                        .setMaxResults(size)
                        .getResultList()
        );
    }

}