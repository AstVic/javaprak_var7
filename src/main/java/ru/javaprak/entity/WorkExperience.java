package ru.javaprak.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "Опыт_работы")
public class WorkExperience {

    @Id
    @Column(name = "ID_опыта")
    private Long id;

    @Column(name = "Компания", nullable = false)
    private String companyName;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_должности", nullable = false)
    private Position position;

    @Column(name = "Дата_начала", nullable = false)
    private LocalDate startDate;

    @Column(name = "Дата_окончания")
    private LocalDate endDate;

    @Column(name = "Зарплата", nullable = false)
    private Long salary;

    public WorkExperience() {
    }

    public WorkExperience(Long id, String companyName, Position position, LocalDate startDate, LocalDate endDate, Long salary) {
        this.id = id;
        this.companyName = companyName;
        this.position = position;
        this.startDate = startDate;
        this.endDate = endDate;
        this.salary = salary;
    }

    public Long getId() {
        return id;
    }

    public Position getPosition() {
        return position;
    }

    public Long getSalary() {
        return salary;
    }
}
