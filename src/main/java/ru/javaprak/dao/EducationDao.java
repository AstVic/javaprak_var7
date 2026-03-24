package ru.javaprak.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ru.javaprak.entity.*;

import java.util.List;

public class EducationDao {

    private final SessionFactory sessionFactory;

    public EducationDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Education> findByLevel(Long levelId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "from Education e where (:levelId is null or e.level.id = :levelId) order by e.id", Education.class)
                    .setParameter("levelId", levelId)
                    .list();
        }
    }
}
