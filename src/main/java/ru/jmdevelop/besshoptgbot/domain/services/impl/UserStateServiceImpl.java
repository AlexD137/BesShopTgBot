package ru.jmdevelop.besshoptgbot.domain.services.impl;

import org.springframework.stereotype.Component;
import ru.jmdevelop.besshoptgbot.domain.services.UserStateService;

@Component
public class UserStateServiceImpl implements UserStateService {



    @Override
    public boolean isUserInForm(Long chatId) {
        return false;
    }
}
