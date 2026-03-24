package ru.javaprak.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "Соискатель")
public class Applicant {

    @Id
    @Column(name = "ID_соискателя")
    private Long id;

    @Column(name = "ФИО", nullable = false)
    private String fullName;

    @Column(name = "Контактная_информация", nullable = false)
    private String contactInfo;

    @Column(name = "Дата_рождения", nullable = false)
    private LocalDate birthDate;

    @Column(name = "Домашний_адрес", nullable = false)
    private String homeAddress;

    @Column(name = "Статус", nullable = false)
    private boolean active;

    public Applicant() {
    }

    public Applicant(Long id, String fullName, String contactInfo, LocalDate birthDate, String homeAddress, boolean active) {
        this.id = id;
        this.fullName = fullName;
        this.contactInfo = contactInfo;
        this.birthDate = birthDate;
        this.homeAddress = homeAddress;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public boolean isActive() {
        return active;
    }
}
