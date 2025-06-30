package ru.jmdevelop.besshoptgbot.handlers.callback;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.jmdevelop.besshoptgbot.handlers.ButtonHandler;
import ru.jmdevelop.besshoptgbot.handlers.FormHandler;
import ru.jmdevelop.besshoptgbot.handlers.keyboards.factory.KeyboardFactory;
import ru.jmdevelop.besshoptgbot.handlers.keyboards.factory.KeyboardType;
import ru.jmdevelop.besshoptgbot.models.entity.UserRequest;
import ru.jmdevelop.besshoptgbot.repo.UserRequestRepository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class FeedbackHandler implements ButtonHandler, FormHandler {

    private final KeyboardFactory keyboardFactory;
    private final UserRequestRepository requestRepository;
    private final Map<Long, UserRequest> activeRequests = new ConcurrentHashMap<>();

    @Override
    public boolean canHandle(String buttonText) {
        return buttonText.equals("😀 Оставить отзыв");

    }

    @Override
    public SendMessage handle(Long chatId) {
        UserRequest request = new UserRequest();
        request.setChatId(chatId);
        request.setRequestType(UserRequest.RequestType.FEEDBACK);
        activeRequests.put(chatId, request);

        return SendMessage.builder()
                .chatId(chatId)
                .text("Пожалуйста, введите ваше имя:")
                .replyMarkup(createCancelKeyboard())
                .build();
    }

    @Override
    public boolean isProcessingForm(Long chatId) {
        return activeRequests.containsKey(chatId);
    }

    @Override
    public SendMessage handleFormInput(Long chatId, String text) {
        if (text.equals("❌ Отмена")) {
            activeRequests.remove(chatId);
            return exitToMainMenu(chatId);
        }

        UserRequest request = activeRequests.get(chatId);

        if (request.getUserName() == null) {
            return processName(chatId, request, text);
        }
        else if (request.getMessage() == null) {
            return processFeedback(chatId, request, text);
        }

        return completeRequest(chatId, request);
    }

    private SendMessage processName(Long chatId, UserRequest request, String name) {
        if (name.trim().length() < 2) {
            return SendMessage.builder()
                    .chatId(chatId)
                    .text("Имя слишком короткое. Введите еще раз:")
                    .replyMarkup(createCancelKeyboard())
                    .build();
        }

        request.setUserName(name.trim());
        return SendMessage.builder()
                .chatId(chatId)
                .text("Напишите ваш отзыв:")
                .replyMarkup(createCancelKeyboard())
                .build();
    }

    private SendMessage processFeedback(Long chatId, UserRequest request, String feedback) {
        if (feedback.trim().length() < 10) {
            return SendMessage.builder()
                    .chatId(chatId)
                    .text("Отзыв слишком короткий (мин. 10 символов). Напишите подробнее:")
                    .replyMarkup(createCancelKeyboard())
                    .build();
        }

        request.setMessage(feedback.trim());
        return completeRequest(chatId, request);
    }

    private SendMessage completeRequest(Long chatId, UserRequest request) {
        try {
            requestRepository.save(request);
            activeRequests.remove(chatId);

            return SendMessage.builder()
                    .chatId(chatId)
                    .text("✅ Спасибо за ваш отзыв! Мы ценим ваше мнение.")
                    .replyMarkup(keyboardFactory.create(KeyboardType.MAIN_MENU, chatId))
                    .build();
        } catch (Exception e) {
            activeRequests.remove(chatId);
            return SendMessage.builder()
                    .chatId(chatId)
                    .text("⚠️ Ошибка при отправке отзыва. Попробуйте позже.")
                    .replyMarkup(keyboardFactory.create(KeyboardType.MAIN_MENU, chatId))
                    .build();
        }
    }

    private ReplyKeyboardMarkup createCancelKeyboard() {
        KeyboardButton cancelButton = new KeyboardButton("❌ Отмена");
        return ReplyKeyboardMarkup.builder()
                .keyboard(List.of(new KeyboardRow(List.of(cancelButton))))
                .resizeKeyboard(true)
                .build();
    }

    private SendMessage exitToMainMenu(Long chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text("Выберите действие:")
                .replyMarkup(keyboardFactory.create(KeyboardType.MAIN_MENU, chatId))
                .build();
    }
}