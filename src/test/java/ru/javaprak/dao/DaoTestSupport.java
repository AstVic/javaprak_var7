package ru.javaprak.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import ru.javaprak.entity.*;

import java.time.LocalDate;

public abstract class DaoTestSupport {

    protected SessionFactory sessionFactory;

    protected void initSessionFactory() {
        Configuration configuration = new Configuration()
                .configure("hibernate.cfg.xml")
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
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);

        seedData();
    }

    protected void closeSessionFactory() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    private void seedData() {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();

            EducationLevel college = new EducationLevel(2L, "Среднее профессиональное");
            EducationLevel bachelor = new EducationLevel(3L, "Бакалавриат");
            session.persist(college);
            session.persist(bachelor);

            Position javaPosition = new Position(1L, "Java-разработчик");
            Position qaPosition = new Position(6L, "QA-инженер");
            session.persist(javaPosition);
            session.persist(qaPosition);

            Company yandex = new Company(1L, "Яндекс", "hr@yandex-team.ru");
            session.persist(yandex);

            Applicant ivanov = new Applicant(1L, "Иванов Иван", "ivanov@mail.ru", LocalDate.of(1999, 4, 12), "Москва", true);
            Applicant petrova = new Applicant(2L, "Петрова Анна", "petrova@mail.ru", LocalDate.of(2001, 11, 3), "Санкт-Петербург", true);
            Applicant smirnov = new Applicant(3L, "Смирнов Дмитрий", "smirnov@mail.ru", LocalDate.of(1997, 2, 25), "Екатеринбург", false);
            session.persist(ivanov);
            session.persist(petrova);
            session.persist(smirnov);

            Resume resume1 = new Resume(1L, ivanov, javaPosition, 180_000L, true);
            Resume resume2 = new Resume(2L, ivanov, javaPosition, 200_000L, false);
            Resume resume3 = new Resume(3L, petrova, qaPosition, 120_000L, true);
            session.persist(resume1);
            session.persist(resume2);
            session.persist(resume3);

            Education edu1 = new Education(1L, bachelor, "МГУ", "Прикладная математика", LocalDate.of(2017, 9, 1), LocalDate.of(2021, 6, 30));
            Education edu2 = new Education(2L, college, "Колледж", "Тестирование", LocalDate.of(2016, 9, 1), LocalDate.of(2019, 6, 30));
            session.persist(edu1);
            session.persist(edu2);

            WorkExperience exp1 = new WorkExperience(1L, "ООО Тех", javaPosition, LocalDate.of(2021, 1, 1), LocalDate.of(2022, 12, 31), 150_000L);
            WorkExperience exp2 = new WorkExperience(2L, "ООО QA", qaPosition, LocalDate.of(2020, 1, 1), null, 110_000L);
            session.persist(exp1);
            session.persist(exp2);

            resume1.getEducationRecords().add(edu1);
            resume3.getEducationRecords().add(edu2);
            resume1.getWorkExperiences().add(exp1);
            resume3.getWorkExperiences().add(exp2);

            Vacancy vacancy1 = new Vacancy(1L, yandex, javaPosition, 250_000L, 24L, bachelor);
            Vacancy vacancy2 = new Vacancy(2L, yandex, javaPosition, 170_000L, 12L, bachelor);
            Vacancy vacancy3 = new Vacancy(3L, yandex, qaPosition, 160_000L, 6L, college);
            session.persist(vacancy1);
            session.persist(vacancy2);
            session.persist(vacancy3);

            Response response1 = new Response(1L, resume1, vacancy1, ResponseStatus.sent, LocalDate.of(2026, 2, 5));
            Response response2 = new Response(2L, resume3, vacancy3, ResponseStatus.recommended, LocalDate.of(2026, 2, 7));
            session.persist(response1);
            session.persist(response2);

            tx.commit();
        }
    }
}
