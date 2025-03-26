package ru.jmdevelop.besshoptgbot.repo.database;

import ru.jmdevelop.besshoptgbot.models.entity.Category;
import ru.jmdevelop.besshoptgbot.repo.CategoryRepository;

import java.util.List;

import static ru.jmdevelop.besshoptgbot.repo.hibernate.HibernateTransactionFactory.inTransaction;

public class CategoryRepositoryDefault implements CategoryRepository {

    @Override
    public List<Category> findAll() {
        String query = "from Category";

        return inTransaction(session -> session.createQuery(query, Category.class).getResultList());
    }

}
