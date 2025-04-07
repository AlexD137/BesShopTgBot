package ru.jmdevelop.besshoptgbot.models.entity;

import jakarta.persistence.*;
import lombok.Data;
import ru.jmdevelop.besshoptgbot.models.dom.MessagePlaceholder;

import java.io.Serial;
import java.io.Serializable;

@Data
@Entity
@Table(name = "messages")
public class Message implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "messages_seq")
    @SequenceGenerator(name = "messages_seq", sequenceName = "messages_id_seq", allocationSize = 1)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(length = 4096, nullable = false)
    private String text;

    public Message() {
    }

    public void applyPlaceholder(MessagePlaceholder placeholder) {
        text = text.replace(placeholder.getPlaceholder(), placeholder.getReplacement().toString());
    }

    public void removeTextBetweenPlaceholder(String placeholderName) {
        text = text.replaceAll(placeholderName + "(?s).*" + placeholderName, "");
    }

    public void removeAllPlaceholders() {
        text = text.replaceAll("%.*%", "");
    }

    public String buildText() {
        removeAllPlaceholders();
        return text;
    }

}