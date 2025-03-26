package ru.jmdevelop.besshoptgbot.repo;

import ru.jmdevelop.besshoptgbot.models.entity.Category;

import java.util.List;

public interface CategoryRepository {

    List<Category> findAll();

}