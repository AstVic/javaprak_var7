package ru.javaprak.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import ru.javaprak.entity.*;

import java.time.LocalDate;
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
            return count > 0;
        }
    }

    public Response create(Long resumeId, Long vacancyId) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Long nextId = session.createQuery("select coalesce(max(r.id), 0) from Response r", Long.class)
                    .uniqueResult() + 1;
            Response response = new Response(
                    nextId,
                    session.get(Resume.class, resumeId),
                    session.get(Vacancy.class, vacancyId),
                    ResponseStatus.sent,
                    LocalDate.now()
            );
            session.persist(response);
            tx.commit();
            return response;
        }
    }
}
