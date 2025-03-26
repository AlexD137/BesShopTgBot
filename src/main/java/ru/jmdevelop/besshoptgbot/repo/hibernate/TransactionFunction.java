package ru.jmdevelop.besshoptgbot.repo.hibernate;

import org.hibernate.Session;

import java.util.function.Function;

@FunctionalInterface
public interface TransactionFunction<T> extends Function<Session, T> {

}