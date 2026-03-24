package ru.javaprak.dao;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EducationDaoTest extends DaoTestSupport {

    private EducationDao educationDao;

    @BeforeEach
    void setUp() {
        initSessionFactory();
        educationDao = new EducationDao(sessionFactory);
    }

    @AfterEach
    void tearDown() {
        closeSessionFactory();
    }

    @Test
    void findByLevelHandlesFilteredAndUnfilteredCases() {
        assertEquals(2, educationDao.findByLevel(null).size());
        assertEquals(1, educationDao.findByLevel(3L).size());
        assertEquals(0, educationDao.findByLevel(99L).size());
    }
}
