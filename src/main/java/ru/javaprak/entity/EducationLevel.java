package ru.javaprak.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "\"Уровень_образования\"")
public class EducationLevel {

    @Id
    @Column(name = "\"ID_уровня\"")
    private Long id;

    @Column(name = "\"Название\"", nullable = false, unique = true)
    private String name;

    public EducationLevel() {
    }

    public EducationLevel(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
