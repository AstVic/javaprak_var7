package ru.javaprak.dao;

import org.junit.jupiter.api.*;
import ru.javaprak.support.DaoTestSupport;

import static org.junit.jupiter.api.Assertions.*;

class ApplicantDaoTest extends DaoTestSupport {

    private ApplicantDao applicantDao;

    @BeforeEach
    void setUp() {
        initSessionFactory();
        applicantDao = new ApplicantDao(sessionFactory);
    }

    @AfterEach
    void tearDown() {
        closeSessionFactory();
    }

    @Test
    void findByIdReturnsApplicantWhenExists() {
        assertTrue(applicantDao.findById(1L).isPresent());
        assertEquals("Иванов Иван", applicantDao.findById(1L).orElseThrow().getFullName());
    }

    @Test
    void findByIdReturnsEmptyWhenNotExists() {
        assertTrue(applicantDao.findById(1000L).isEmpty());
    }

    @Test
    void findByStatusReturnsCorrectBranches() {
        assertEquals(2, applicantDao.findByStatus(true).size());
        assertEquals(1, applicantDao.findByStatus(false).size());
    }
}
