package ru.javaprak.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ru.javaprak.entity.*;

import java.util.List;

public class ResponseDao {

    private final SessionFactory sessionFactory;

    public ResponseDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Response> findByResumeId(Long resumeId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "from Response r where r.resume.id = :resumeId order by r.id", Response.class)
                    .setParameter("resumeId", resumeId)
                    .list();
        }
    }

    public boolean existsByResumeAndVacancy(Long resumeId, Long vacancyId) {
        try (Session session = sessionFactory.openSession()) {
            Long count = session.createQuery(
                            "select count(r.id) from Response r where r.resume.id = :resumeId and r.vacancy.id = :vacancyId",
                            Long.class)
                    .setParameter("resumeId", resumeId)
                    .setParameter("vacancyId", vacancyId)
                    .uniqueResult();
            return count != null && count > 0;
        }
    }
}
