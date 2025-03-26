package ru.jmdevelop.besshoptgbot.handlers.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.jmdevelop.besshoptgbot.handlers.UpdateHandler;
import ru.jmdevelop.besshoptgbot.models.dom.Button;
import ru.jmdevelop.besshoptgbot.models.dom.Command;
import ru.jmdevelop.besshoptgbot.models.entity.Client;
import ru.jmdevelop.besshoptgbot.repo.ClientRepository;
import ru.jmdevelop.besshoptgbot.services.MessageService;

public class StartCommandHandler implements UpdateHandler {

    private final ClientRepository clientRepository;
    private final MessageService messageService;

    public StartCommandHandler(
            ClientRepository clientRepository,
            MessageService messageService) {

        this.clientRepository = clientRepository;
        this.messageService = messageService;
    }

    @Override
    public Command getCommand() {
        return Command.START;
    }

    @Override
    public boolean canHandleUpdate(Update update) {
        return update.hasMessage() &&
                update.getMessage().hasText() &&
                update.getMessage().getText().startsWith(Button.START.getAlias());
    }

    @Override
    public void handleUpdate(AbsSender absSender, Update update) throws TelegramApiException {
        Long chatId = update.getMessage().getChatId();

        saveClient(chatId);
        sendStartMessage(absSender, chatId);
    }

    private void saveClient(Long chatId) {
        Client client = clientRepository.findByChatId(chatId);

        if (client == null) {
            createClient(chatId);
        } else if (!client.isActive()) {
            activateClient(client);
        }
    }

    private void createClient(Long chatId) {
        Client client = new Client();
        client.setChatId(chatId);
        client.setActive(true);
        clientRepository.save(client);
    }

    private void activateClient(Client client) {
        client.setActive(true);
        clientRepository.update(client);
    }

    private void sendStartMessage(AbsSender absSender, Long chatId) throws TelegramApiException {
        String text = messageService.findByName("START_MESSAGE").buildText();

        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .replyMarkup(Button.createGeneralMenuKeyboard())
                .build();
        absSender.execute(message);
    }

}