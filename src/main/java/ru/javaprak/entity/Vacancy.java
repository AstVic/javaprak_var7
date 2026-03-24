package ru.javaprak.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Вакансия")
public class Vacancy {

    @Id
    @Column(name = "ID_вакансии")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_компании", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_должности", nullable = false)
    private Position position;

    @Column(name = "Зарплата", nullable = false)
    private Long salary;

    @Column(name = "Мин_стаж_месяцев", nullable = false)
    private Long minMonthsExperience;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Мин_уровень_образования", nullable = false)
    private EducationLevel minEducationLevel;

    public Vacancy() {
    }

    public Vacancy(Long id, Company company, Position position, Long salary, Long minMonthsExperience, EducationLevel minEducationLevel) {
        this.id = id;
        this.company = company;
        this.position = position;
        this.salary = salary;
        this.minMonthsExperience = minMonthsExperience;
        this.minEducationLevel = minEducationLevel;
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

    public Long getMinMonthsExperience() {
        return minMonthsExperience;
    }

    public EducationLevel getMinEducationLevel() {
        return minEducationLevel;
    }
}
