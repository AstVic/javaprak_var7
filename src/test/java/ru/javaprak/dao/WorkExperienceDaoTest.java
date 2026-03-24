package ru.javaprak.dao;

import org.junit.jupiter.api.*;
import ru.javaprak.support.DaoTestSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WorkExperienceDaoTest extends DaoTestSupport {

    private WorkExperienceDao workExperienceDao;

    @BeforeEach
    void setUp() {
        initSessionFactory();
        workExperienceDao = new WorkExperienceDao(sessionFactory);
    }

    @AfterEach
    void tearDown() {
        closeSessionFactory();
    }

    @Test
    void findByPositionAndMinSalaryCoversBranches() {
        assertEquals(2, workExperienceDao.findByPositionAndMinSalary(null, null).size());
        assertEquals(1, workExperienceDao.findByPositionAndMinSalary(1L, 120_000L).size());
        assertEquals(0, workExperienceDao.findByPositionAndMinSalary(6L, 200_000L).size());
    }
}
