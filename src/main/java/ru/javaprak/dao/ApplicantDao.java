package ru.javaprak.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ru.javaprak.entity.*;

import java.util.List;
import java.util.Optional;

public class ApplicantDao {

    private final SessionFactory sessionFactory;

    public ApplicantDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Optional<Applicant> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.get(Applicant.class, id));
        }
    }

    public List<Applicant> findByStatus(boolean isActive) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Applicant a where a.active = :active order by a.id", Applicant.class)
                    .setParameter("active", isActive)
                    .list();
        }
    }
}
