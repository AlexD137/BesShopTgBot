package ru.jmdevelop.besshoptgbot.repo.hibernate;

import org.hibernate.Session;

import java.util.function.Consumer;

@FunctionalInterface
public interface TransactionVoidFunction extends Consumer<Session> {

}
