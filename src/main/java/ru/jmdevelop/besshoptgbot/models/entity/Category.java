package ru.jmdevelop.besshoptgbot.models.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.io.Serial;
import java.io.Serializable;



@Data
@Entity
@Table(name = "categories")
public class Category implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "categories_seq")
    @SequenceGenerator(name = "categories_seq", sequenceName = "categories_id_seq", allocationSize = 1)
    private Integer id;

    @Column(nullable = false)
    private String name;

}