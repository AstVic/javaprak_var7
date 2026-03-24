package ru.javaprak.dao;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class ResponseDaoTest extends DaoTestSupport {

    private ResponseDao responseDao;

    @BeforeEach
    void setUp() {
        initSessionFactory();
        responseDao = new ResponseDao(sessionFactory);
    }

    @AfterEach
    void tearDown() {
        closeSessionFactory();
    }

    @Test
    void findByResumeIdReturnsBothFoundAndNotFound() {
        assertEquals(1, responseDao.findByResumeId(1L).size());
        assertTrue(responseDao.findByResumeId(2L).isEmpty());
    }

    @Test
    void existsByResumeAndVacancyReturnsTrueAndFalse() {
        assertTrue(responseDao.existsByResumeAndVacancy(1L, 1L));
        assertFalse(responseDao.existsByResumeAndVacancy(1L, 3L));
    }
}
