package ru.javaprak.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.javaprak.entity.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ResumeDaoTest extends DaoTestSupport {

    private ResumeDao resumeDao;

    @BeforeEach
    void setUp() {
        initSessionFactory();
        resumeDao = new ResumeDao(sessionFactory);
    }

    @AfterEach
    void tearDown() {
        closeSessionFactory();
    }

    @Test
    void findByIdReturnsResumeWhenExists() {
        Optional<Resume> resumeOpt = resumeDao.findById(1L);

        assertTrue(resumeOpt.isPresent());
        Resume resume = resumeOpt.get();
        assertEquals(1L, resume.getId());
        assertTrue(resume.isActive());
        assertEquals(180_000L, resume.getMinSalary());
    }

    @Test
    void findByIdReturnsEmptyWhenNotExists() {
        Optional<Resume> resumeOpt = resumeDao.findById(999L);

        assertTrue(resumeOpt.isEmpty());
    }

    @Test
    void findActiveByFiltersReturnsOnlyActiveAndMatching() {
        List<Resume> resumes = resumeDao.findActiveByFilters(1L, 150_000L, 200_000L);

        assertEquals(1, resumes.size());
        assertEquals(1L, resumes.getFirst().getId());
        assertTrue(resumes.getFirst().isActive());
    }

    @Test
    void findActiveByFiltersReturnsAllActiveWhenFiltersAreNull() {
        List<Resume> resumes = resumeDao.findActiveByFilters(null, null, null);

        assertEquals(2, resumes.size());
        assertEquals(List.of(1L, 3L), resumes.stream().map(Resume::getId).toList());
    }
}
