package ru.jmdevelop.besshoptgbot.models.dom;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@ToString
@RequiredArgsConstructor
public class ClientAction implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final Command command;
    private final String action;
    private final LocalDateTime createdTime = LocalDateTime.now();

}