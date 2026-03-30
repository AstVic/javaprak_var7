package ru.javaprak.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.javaprak.entity.*;
import ru.javaprak.support.DaoTestSupport;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VacancyDaoTest extends DaoTestSupport {

    private VacancyDao vacancyDao;

    @BeforeEach
    void setUp() {
        initSessionFactory();
        vacancyDao = new VacancyDao(sessionFactory);
    }

    @AfterEach
    void tearDown() {
        closeSessionFactory();
    }

    @Test
    void findByMinSalaryAndPositionUsesBothFilters() {
        List<Vacancy> vacancies = vacancyDao.findByMinSalaryAndPosition(180_000L, 1L);

        assertEquals(1, vacancies.size());
        assertEquals(1L, vacancies.get(0).getId());
        assertEquals(250_000L, vacancies.get(0).getSalary());
    }

    @Test
    void findByMinSalaryAndPositionReturnsAllWhenFiltersAreNull() {
        List<Vacancy> vacancies = vacancyDao.findByMinSalaryAndPosition(null, null);

        assertEquals(3, vacancies.size());
        assertEquals(List.of(1L, 2L, 3L), vacancies.stream().map(Vacancy::getId).toList());
    }

    @Test
    void findByMinSalaryAndPositionHandlesSingleFilterCases() {
        List<Vacancy> byPositionOnly = vacancyDao.findByMinSalaryAndPosition(null, 1L);
        List<Vacancy> bySalaryOnly = vacancyDao.findByMinSalaryAndPosition(180_000L, null);

        assertEquals(List.of(1L, 2L), byPositionOnly.stream().map(Vacancy::getId).toList());
        assertEquals(List.of(1L), bySalaryOnly.stream().map(Vacancy::getId).toList());
    }

    @Test
    void findByMinSalaryAndPositionReturnsEmptyWhenNoMatches() {
        List<Vacancy> vacancies = vacancyDao.findByMinSalaryAndPosition(1_000_000L, 1L);

        assertTrue(vacancies.isEmpty());
    }

    @Test
    void findMatchingForResumeReturnsVacanciesForActiveResume() {
        List<Vacancy> vacancies = vacancyDao.findMatchingForResume(1L);

        assertEquals(1, vacancies.size());
        Vacancy vacancy = vacancies.get(0);
        assertEquals(1L, vacancy.getId());
        assertTrue(vacancy.getSalary() >= 180_000L);
        assertEquals(1L, vacancy.getPosition().getId());
    }

    @Test
    void findMatchingForResumeReturnsEmptyForInactiveResume() {
        assertTrue(vacancyDao.findMatchingForResume(2L).isEmpty());
    }

    @Test
    void findMatchingForResumeReturnsEmptyForMissingResume() {
        assertTrue(vacancyDao.findMatchingForResume(777L).isEmpty());
    }

    @Test
    void findMatchingForResumeReturnsEmptyWhenNoVacancyMatchesSalary() {
        saveHighSalaryActiveResume();

        List<Vacancy> vacancies = vacancyDao.findMatchingForResume(10L);

        assertTrue(vacancies.isEmpty());
    }

    private void saveHighSalaryActiveResume() {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();

            Applicant applicant = session.get(Applicant.class, 1L);
            Position position = session.get(Position.class, 1L);
            Resume highSalaryResume = new Resume(10L, applicant, position, 500_000L, true);
            session.persist(highSalaryResume);

            tx.commit();
        }
    }
}