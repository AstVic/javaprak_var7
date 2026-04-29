package ru.javaprak.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ru.javaprak.entity.Applicant;
import ru.javaprak.entity.Company;
import ru.javaprak.entity.EducationLevel;
import ru.javaprak.entity.Position;

import java.util.List;

public class ReferenceDao {

    private final SessionFactory sessionFactory;

    public ReferenceDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Position> findPositions() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Position p order by p.id", Position.class).list();
        }
    }

    public List<EducationLevel> findEducationLevels() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from EducationLevel e order by e.id", EducationLevel.class).list();
        }
    }

    public List<Company> findCompanies() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Company c order by c.id", Company.class).list();
        }
    }

    public List<Applicant> findApplicants() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Applicant a order by a.id", Applicant.class).list();
        }
    }
}
