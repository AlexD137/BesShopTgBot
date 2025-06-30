package ru.jmdevelop.besshoptgbot.core;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.jmdevelop.besshoptgbot.handlers.UpdateDispatcher;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class BotUpdateHandler implements LongPollingUpdateConsumer {

    private final UpdateDispatcher dispatcher;




    @Override
    public void consume(List<Update> updates) {
        updates.forEach(update -> {
            try {
                log.debug("Processing update ID: {}", update.getUpdateId());
                dispatcher.dispatch(update);
            } catch (Exception e) {
                log.error("Error processing update {}", update.getUpdateId(), e);
               e.printStackTrace();//TODO
            }
        });
    }
}