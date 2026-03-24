package ru.javaprak.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "\"Компания\"")
public class Company {

    @Id
    @Column(name = "\"ID_компании\"")
    private Long id;

    @Column(name = "\"Название\"", nullable = false)
    private String name;

    @Column(name = "\"Контактная_информация\"", nullable = false)
    private String contactInfo;

    public Company() {
    }

    public Company(Long id, String name, String contactInfo) {
        this.id = id;
        this.name = name;
        this.contactInfo = contactInfo;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
