package ru.javaprak.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ru.javaprak.entity.*;

import java.util.List;
import java.util.Optional;

public class ResumeDao {

    private final SessionFactory sessionFactory;

    public ResumeDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Optional<Resume> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.get(Resume.class, id));
        }
    }

    public List<Resume> findActiveByFilters(Long positionId, Long minSalary, Long maxSalary) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            """
                                    select r
                                    from Resume r
                                    where r.active = true
                                      and (:positionId is null or r.position.id = :positionId)
                                      and (:minSalary is null or r.minSalary >= :minSalary)
                                      and (:maxSalary is null or r.minSalary <= :maxSalary)
                                    order by r.id
                                    """, Resume.class)
                    .setParameter("positionId", positionId)
                    .setParameter("minSalary", minSalary)
                    .setParameter("maxSalary", maxSalary)
                    .list();
        }
    }
}
