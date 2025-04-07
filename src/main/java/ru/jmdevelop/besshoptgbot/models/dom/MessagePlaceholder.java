package ru.jmdevelop.besshoptgbot.models.dom;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Objects;

@Getter
@ToString
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class MessagePlaceholder {
    private final String placeholder;
    private final Object replacement;

    public static MessagePlaceholder of(String placeholder, Object replacement) {
        return new MessagePlaceholder(
                Objects.requireNonNull(placeholder, "Placeholder cannot be null"),
                Objects.requireNonNull(replacement, "Replacement cannot be null")
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessagePlaceholder that = (MessagePlaceholder) o;
        return Objects.equals(placeholder, that.placeholder) &&
                Objects.equals(replacement, that.replacement);
    }

    @Override
    public int hashCode() {
        return Objects.hash(placeholder, replacement);
    }
}