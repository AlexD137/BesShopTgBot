package ru.jmdevelop.besshoptgbot.models.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Data
@Entity
@Table(name = "clients")
public class Client implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "clients_seq")
    @SequenceGenerator(name = "clients_seq", sequenceName = "clients_id_seq", allocationSize = 1)
    private Integer id;

    @Column(name = "chat_id", unique = true, nullable = false)
    private Long chatId;

    @Column
    private String name;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column
    private String city;

    @Column
    private String address;

    @Column(name = "is_active", nullable = false)
    private boolean active;

}