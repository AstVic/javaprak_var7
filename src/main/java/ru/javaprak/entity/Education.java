package ru.javaprak.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "Образование")
public class Education {

    @Id
    @Column(name = "ID_образования")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Уровень", nullable = false)
    private EducationLevel level;

    @Column(name = "Место_учебы", nullable = false)
    private String institution;

    @Column(name = "Направление", nullable = false)
    private String major;

    @Column(name = "Дата_начала", nullable = false)
    private LocalDate startDate;

    @Column(name = "Дата_окончания")
    private LocalDate endDate;

    public Education() {
    }

    public Education(Long id, EducationLevel level, String institution, String major, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.level = level;
        this.institution = institution;
        this.major = major;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getId() {
        return id;
    }

    public EducationLevel getLevel() {
        return level;
    }
}
