package ru.javaprak.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "Отклик")
public class Response {

    @Id
    @Column(name = "ID_отклика")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_резюме", nullable = false)
    private Resume resume;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_вакансии", nullable = false)
    private Vacancy vacancy;

    @Enumerated(EnumType.STRING)
    @Column(name = "Статус", nullable = false)
    private ResponseStatus status;

    @Column(name = "Дата", nullable = false)
    private LocalDate date;

    public Response() {
    }

    public Response(Long id, Resume resume, Vacancy vacancy, ResponseStatus status, LocalDate date) {
        this.id = id;
        this.resume = resume;
        this.vacancy = vacancy;
        this.status = status;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public Resume getResume() {
        return resume;
    }

    public Vacancy getVacancy() {
        return vacancy;
    }

    public ResponseStatus getStatus() {
        return status;
    }
}
