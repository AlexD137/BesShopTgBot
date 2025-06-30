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
public class CallbackHandler implements ButtonHandler, FormHandler {
    private final KeyboardFactory keyboardFactory;
    private final UserRequestRepository requestRepository;
    private final Map<Long, UserRequest> activeForms = new ConcurrentHashMap<>();

    @Override
    public boolean canHandle(String buttonText) {
        return buttonText.equals("📞 Заказать звонок");
    }

    @Override
    public SendMessage handle(Long chatId) {
        UserRequest request = new UserRequest();
        request.setChatId(chatId);
        request.setRequestType(UserRequest.RequestType.CALL);
        activeForms.put(chatId, request);

        return SendMessage.builder()
                .chatId(chatId)
                .text("Введите ваше имя:")
                .replyMarkup(createCancelKeyboard())
                .build();
    }

    @Override
    public boolean isProcessingForm(Long chatId) {
        return activeForms.containsKey(chatId);
    }

    @Override
    public SendMessage handleFormInput(Long chatId, String text) {
        if (text.equals("❌ Отмена")) {
            activeForms.remove(chatId);
            return exitToSupportMenu(chatId);
        }

        UserRequest request = activeForms.get(chatId);

        if (request.getUserName() == null) {
            return processName(chatId, request, text);
        }
        else if (request.getPhone() == null) {
            return processPhone(chatId, request, text);
        }

        return exitToSupportMenu(chatId);
    }

    private SendMessage processName(Long chatId, UserRequest request, String text) {
        if (text.length() < 2) {
            return SendMessage.builder()
                    .chatId(chatId)
                    .text("Имя слишком короткое. Введите еще раз:")
                    .replyMarkup(createCancelKeyboard())
                    .build();
        }

        request.setUserName(text);
        return SendMessage.builder()
                .chatId(chatId)
                .text("Теперь введите ваш телефон (+71234567890):")
                .replyMarkup(createCancelKeyboard())
                .build();
    }

    private SendMessage processPhone(Long chatId, UserRequest request, String text) {
        if (!text.matches("^\\+7\\d{10}$")) {
            return SendMessage.builder()
                    .chatId(chatId)
                    .text("Неверный формат. Пример: +79123456789")
                    .replyMarkup(createCancelKeyboard())
                    .build();
        }

        request.setPhone(text);
        return completeRequest(chatId, request);
    }

    private SendMessage completeRequest(Long chatId, UserRequest request) {
        try {
            requestRepository.save(request);
            activeForms.remove(chatId);

            return SendMessage.builder()
                    .chatId(chatId)
                    .text("✅ Заявка принята! Мы скоро вам перезвоним")
                    .replyMarkup(keyboardFactory.create(KeyboardType.SUPPORT, chatId))
                    .build();
        } catch (Exception e) {
            activeForms.remove(chatId);
            return SendMessage.builder()
                    .chatId(chatId)
                    .text("⚠️ Ошибка сохранения. Попробуйте позже")
                    .replyMarkup(keyboardFactory.create(KeyboardType.SUPPORT, chatId))
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

    private SendMessage exitToSupportMenu(Long chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text("Выберите действие:")
                .replyMarkup(keyboardFactory.create(KeyboardType.SUPPORT, chatId))
                .build();
    }
}