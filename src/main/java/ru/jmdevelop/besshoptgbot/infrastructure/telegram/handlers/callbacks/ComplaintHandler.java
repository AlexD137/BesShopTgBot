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
public class ComplaintHandler implements ButtonHandler, FormHandler {

    private final KeyboardFactory keyboardFactory;
    private final UserRequestRepository requestRepository;
    private final Map<Long, UserRequest> activeRequests = new ConcurrentHashMap<>();

    @Override
    public boolean canHandle(String buttonText) {
        return buttonText.equals("\uD83D\uDE9A Жалоба на доставку") ||
                buttonText.equals("\uD83E\uDDD0 Жалоба на качество");

    }

    @Override
    public SendMessage handle(Long chatId) {
        UserRequest request = new UserRequest();
        request.setChatId(chatId);
        request.setRequestType(UserRequest.RequestType.COMPLAINT);
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
        else if (request.getPhone() == null) {
            return processPhone(chatId, request, text);
        }
        else if (request.getMessage() == null) {
            return processComplaint(chatId, request, text);
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
                .text("Введите ваш номер телефона для связи:")
                .replyMarkup(createCancelKeyboard())
                .build();
    }

    private SendMessage processPhone(Long chatId, UserRequest request, String phone) {
        if (phone.trim().length() < 5 || !phone.matches(".*\\d.*")) {
            return SendMessage.builder()
                    .chatId(chatId)
                    .text("Номер телефона слишком короткий или не содержит цифр. Введите еще раз:")
                    .replyMarkup(createCancelKeyboard())
                    .build();
        }

        request.setPhone(phone.trim());
        return SendMessage.builder()
                .chatId(chatId)
                .text("Опишите вашу жалобу:")
                .replyMarkup(createCancelKeyboard())
                .build();
    }

    private SendMessage processComplaint(Long chatId, UserRequest request, String complaint) {
        if (complaint.trim().length() < 10) {
            return SendMessage.builder()
                    .chatId(chatId)
                    .text("Описание слишком короткое (мин. 10 символов). Опишите подробнее:")
                    .replyMarkup(createCancelKeyboard())
                    .build();
        }

        request.setMessage(complaint.trim());
        return completeRequest(chatId, request);
    }

    private SendMessage completeRequest(Long chatId, UserRequest request) {
        try {
            requestRepository.save(request);
            activeRequests.remove(chatId);

            return SendMessage.builder()
                    .chatId(chatId)
                    .text("✅ Ваша жалоба принята! Мы свяжемся с вами в ближайшее время.")
                    .replyMarkup(keyboardFactory.create(KeyboardType.MAIN_MENU, chatId))
                    .build();
        } catch (Exception e) {
            activeRequests.remove(chatId);
            return SendMessage.builder()
                    .chatId(chatId)
                    .text("⚠️ Ошибка при отправке жалобы. Попробуйте позже.")
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