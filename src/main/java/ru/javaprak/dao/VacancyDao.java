package ru.javaprak.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ru.javaprak.entity.*;

import java.util.Collections;
import java.util.List;

public class VacancyDao {

    private final SessionFactory sessionFactory;

    public VacancyDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Vacancy> findByMinSalaryAndPosition(Long minSalary, Long positionId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            """
                                    select v
                                    from Vacancy v
                                    where (:positionId is null or v.position.id = :positionId)
                                      and (:minSalary is null or v.salary >= :minSalary)
                                    order by v.salary desc, v.id
                                    """, Vacancy.class)
                    .setParameter("positionId", positionId)
                    .setParameter("minSalary", minSalary)
                    .list();
        }
    }

    public List<Vacancy> findMatchingForResume(Long resumeId) {
        try (Session session = sessionFactory.openSession()) {
            Resume resume = session.get(Resume.class, resumeId);
            if (resume == null || !resume.isActive()) {
                return Collections.emptyList();
            }

            return session.createQuery(
                            """
                                    select v
                                    from Vacancy v
                                    where v.position.id = :positionId
                                      and v.salary >= :expectedSalary
                                    order by v.salary desc, v.id
                                    """, Vacancy.class)
                    .setParameter("positionId", resume.getPosition().getId())
                    .setParameter("expectedSalary", resume.getMinSalary())
                    .list();
        }
    }
}
