package ru.jmdevelop.besshoptgbot.repo;

import ru.jmdevelop.besshoptgbot.models.dom.ClientOrder;

public interface ClientOrderStateRepository {

    ClientOrder findByChatId(Long chatId);

    void updateByChatId(Long chatId, ClientOrder clientOrder);

    void deleteByChatId(Long chatId);

}