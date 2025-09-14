package ru.jmdevelop.besshoptgbot.infrastructure.telegram.handlers.callbacks;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.jmdevelop.besshoptgbot.infrastructure.telegram.handlers.ButtonHandler;
import ru.jmdevelop.besshoptgbot.infrastructure.telegram.handlers.FormHandler;
import ru.jmdevelop.besshoptgbot.infrastructure.telegram.handlers.keyboards.factory.KeyboardFactory;
import ru.jmdevelop.besshoptgbot.infrastructure.telegram.handlers.keyboards.factory.KeyboardType;
import ru.jmdevelop.besshoptgbot.infrastructure.persistence.entity.UserRequest;
import ru.jmdevelop.besshoptgbot.domain.repo.UserRequestRepository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class HotlineHandler implements ButtonHandler, FormHandler {

    private final KeyboardFactory keyboardFactory;
    private final UserRequestRepository requestRepository;
    private final Map<Long, UserRequest> activeRequests = new ConcurrentHashMap<>();

    @Override
    public boolean canHandle(String buttonText) {
        return buttonText.equals("✉️ Написать на горячую линию");
    }

    @Override
    public SendMessage handle(Long chatId) {
        UserRequest request = new UserRequest();
        request.setChatId(chatId);
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
        else if (request.getCity() == null) {
            return processCity(chatId, request, text);
        }
        else if (request.getMessage() == null) {
            return processProblem(chatId, request, text);
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
                .text("Из какого вы города?")
                .replyMarkup(createCancelKeyboard())
                .build();
    }

    private SendMessage processCity(Long chatId, UserRequest request, String city) {
        if (city.trim().length() < 2) {
            return SendMessage.builder()
                    .chatId(chatId)
                    .text("Название города слишком короткое. Введите еще раз:")
                    .replyMarkup(createCancelKeyboard())
                    .build();
        }

        request.setCity(city.trim());
        return SendMessage.builder()
                .chatId(chatId)
                .text("Опишите вашу проблему:")
                .replyMarkup(createCancelKeyboard())
                .build();
    }

    private SendMessage processProblem(Long chatId, UserRequest request, String problem) {
        if (problem.trim().length() < 10) {
            return SendMessage.builder()
                    .chatId(chatId)
                    .text("Описание слишком короткое (мин. 10 символов). Опишите подробнее:")
                    .replyMarkup(createCancelKeyboard())
                    .build();
        }

        request.setMessage(problem.trim());
        return completeRequest(chatId, request);
    }

    private SendMessage completeRequest(Long chatId, UserRequest request) {
        try {
            requestRepository.save(request);
            activeRequests.remove(chatId);

            return SendMessage.builder()
                    .chatId(chatId)
                    .text("✅ Ваше обращение принято! Мы свяжемся с вами в ближайшее время.")
                    .replyMarkup(keyboardFactory.create(KeyboardType.MAIN_MENU, chatId))
                    .build();
        } catch (Exception e) {
            activeRequests.remove(chatId);
            return SendMessage.builder()
                    .chatId(chatId)
                    .text("⚠️ Ошибка при отправке обращения. Попробуйте позже.")
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