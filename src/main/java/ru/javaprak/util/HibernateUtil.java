package ru.javaprak.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import ru.javaprak.entity.Applicant;
import ru.javaprak.entity.Company;
import ru.javaprak.entity.Education;
import ru.javaprak.entity.EducationLevel;
import ru.javaprak.entity.Position;
import ru.javaprak.entity.Response;
import ru.javaprak.entity.Resume;
import ru.javaprak.entity.Vacancy;
import ru.javaprak.entity.WorkExperience;

public final class HibernateUtil {

    private static final SessionFactory SESSION_FACTORY = buildSessionFactory();

    private HibernateUtil() {
    }

    private static SessionFactory buildSessionFactory() {
        Configuration configuration = new Configuration()
                .configure()
                .addAnnotatedClass(EducationLevel.class)
                .addAnnotatedClass(Position.class)
                .addAnnotatedClass(Applicant.class)
                .addAnnotatedClass(Company.class)
                .addAnnotatedClass(Education.class)
                .addAnnotatedClass(WorkExperience.class)
                .addAnnotatedClass(Response.class)
                .addAnnotatedClass(Resume.class)
                .addAnnotatedClass(Vacancy.class);
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();
        return configuration.buildSessionFactory(serviceRegistry);
    }

    public static SessionFactory getSessionFactory() {
        return SESSION_FACTORY;
    }
}
