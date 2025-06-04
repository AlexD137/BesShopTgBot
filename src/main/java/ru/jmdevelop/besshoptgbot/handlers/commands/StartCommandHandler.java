package ru.jmdevelop.besshoptgbot.handlers.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.jmdevelop.besshoptgbot.handlers.UpdateHandler;
import ru.jmdevelop.besshoptgbot.handlers.keyboards.factory.KeyboardFactory;
import ru.jmdevelop.besshoptgbot.handlers.keyboards.factory.KeyboardType;
import ru.jmdevelop.besshoptgbot.models.Command;
import ru.jmdevelop.besshoptgbot.models.HandlerPriority;
import ru.jmdevelop.besshoptgbot.models.entity.Client;
import ru.jmdevelop.besshoptgbot.repo.jpa.ClientRepository;
import ru.jmdevelop.besshoptgbot.services.MessageService;
import ru.jmdevelop.besshoptgbot.services.UserContextService;


@Component
@RequiredArgsConstructor
public class StartCommandHandler implements UpdateHandler {
    private final ClientRepository clientRepository;
    private final MessageService messageService;
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
    public int getPriority() {
        return HandlerPriority.SYSTEM.getPriority();
    }

    @Override
    public void handle(Update update, AbsSender sender) throws TelegramApiException {
        Long chatId = update.getMessage().getChatId();

        processClient(chatId);
        sendWelcomeMessage(chatId, sender);
        updateUserContext(chatId);
    }

    private void processClient(Long chatId) {
        clientRepository.findByChatId(chatId)
                .ifPresentOrElse(
                        this::activateClientIfNeeded,
                        () -> createNewClient(chatId)
                );
    }

    private void activateClientIfNeeded(Client client) {
        if (!client.isActive()) {
            client.setActive(true);
            clientRepository.save(client);
        }
    }

    private void createNewClient(Long chatId) {
        Client client = new Client();
        client.setChatId(chatId);
        client.setActive(true);
        clientRepository.save(client);
    }

    private void sendWelcomeMessage(Long chatId, AbsSender sender) throws TelegramApiException {
        SendMessage message = SendMessage.builder()
                .chatId(chatId.toString())
                .text(messageService.findByName("START_MESSAGE").buildText())
                .replyMarkup(keyboardFactory.create(KeyboardType.MAIN_MENU, chatId))
                .build();

        sender.execute(message);
    }

    private void updateUserContext(Long chatId) {
        userContextService.resetContext(chatId);
        userContextService.pushState(chatId, KeyboardType.MAIN_MENU);
        userContextService.putTempData(chatId, "first_launch", true);
    }
}