package ru.javaprak.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Резюме")
public class Resume {

    @Id
    @Column(name = "ID_резюме")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Соискатель", nullable = false)
    private Applicant applicant;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_должности", nullable = false)
    private Position position;

    @Column(name = "Желаемая_зарплата_мин", nullable = false)
    private Long minSalary;

    @Column(name = "Актуально", nullable = false)
    private boolean active;

    @ManyToMany
    @JoinTable(name = "Резюме_Образование",
            joinColumns = @JoinColumn(name = "ID_резюме"),
            inverseJoinColumns = @JoinColumn(name = "ID_образования"))
    private Set<Education> educationRecords = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "Резюме_Опыт",
            joinColumns = @JoinColumn(name = "ID_резюме"),
            inverseJoinColumns = @JoinColumn(name = "ID_опыта"))
    private Set<WorkExperience> workExperiences = new HashSet<>();

    public Resume() {
    }

    public Resume(Long id, Applicant applicant, Position position, Long minSalary, boolean active) {
        this.id = id;
        this.applicant = applicant;
        this.position = position;
        this.minSalary = minSalary;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public Applicant getApplicant() {
        return applicant;
    }

    public Position getPosition() {
        return position;
    }

    public Long getMinSalary() {
        return minSalary;
    }

    public boolean isActive() {
        return active;
    }

    public Set<Education> getEducationRecords() {
        return educationRecords;
    }

    public Set<WorkExperience> getWorkExperiences() {
        return workExperiences;
    }
}
