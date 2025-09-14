package ru.jmdevelop.besshoptgbot.infrastructure.telegram.handlers.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.jmdevelop.besshoptgbot.infrastructure.telegram.handlers.UpdateHandler;
import ru.jmdevelop.besshoptgbot.infrastructure.telegram.handlers.keyboards.factory.KeyboardFactory;
import ru.jmdevelop.besshoptgbot.infrastructure.telegram.handlers.keyboards.factory.KeyboardType;
import ru.jmdevelop.besshoptgbot.domain.models.Command;
import ru.jmdevelop.besshoptgbot.infrastructure.persistence.entity.Client;
import ru.jmdevelop.besshoptgbot.domain.repo.jpa.UserRepository;
import ru.jmdevelop.besshoptgbot.domain.services.UserContextService;


@Component
@RequiredArgsConstructor
public class StartCommandHandler implements UpdateHandler {
    private final UserRepository clientRepository;
    private final KeyboardFactory keyboardFactory;
    private final UserContextService userContextService;

    @Override
    public boolean canHandle(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return false;
        }
        return Command.START.getCommand().equals(update.getMessage().getText().trim());
    }

    @Override
    public BotApiMethod<?> handle(Update update) {
        User user = update.getMessage().getFrom();
        Long userId = user.getId();

        processClient(user);
        updateUserContext(userId);

        return createWelcomeMessage(userId);
    }

    private void processClient(User user) {
        clientRepository.findByUserId(user.getId())
                .ifPresentOrElse(
                        this::activateClientIfNeeded,
                        () -> createNewClient(user)
                );
    }

    private void activateClientIfNeeded(Client client) {
        if (!client.isActive()) {
            client.setActive(true);
            clientRepository.save(client);
        }
    }

    private void createNewClient(User user) {
        Client client = new Client();
        client.setTelegramUsername(user.getUserName());
        client.setFirstName(user.getFirstName());
        client.setLastName(user.getLastName());
        client.setUserId(user.getId());
        client.setActive(true);
        clientRepository.save(client);
    }

    private SendMessage createWelcomeMessage(Long chatId) {
        return SendMessage.builder()
                .chatId(chatId.toString())
                .text("Добро пожаловать в наш магазин!")
                .replyMarkup(keyboardFactory.create(KeyboardType.MAIN_MENU, chatId))
                .build();
    }

    private void updateUserContext(Long chatId) {
        userContextService.resetMenuHistory(chatId);
        userContextService.pushMenuState(chatId, KeyboardType.MAIN_MENU);
    }
}