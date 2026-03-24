package ru.javaprak.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ru.javaprak.entity.*;

import java.util.List;

public class WorkExperienceDao {

    private final SessionFactory sessionFactory;

    public WorkExperienceDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<WorkExperience> findByPositionAndMinSalary(Long positionId, Long minSalary) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            """
                                    from WorkExperience w
                                    where (:positionId is null or w.position.id = :positionId)
                                      and (:minSalary is null or w.salary >= :minSalary)
                                    order by w.id
                                    """, WorkExperience.class)
                    .setParameter("positionId", positionId)
                    .setParameter("minSalary", minSalary)
                    .list();
        }
    }
}
