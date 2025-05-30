package ru.jmdevelop.besshoptgbot.repo.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.jmdevelop.besshoptgbot.models.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}