package ru.javaprak.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ru.javaprak.entity.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class VacancyDao {

    private final SessionFactory sessionFactory;

    public VacancyDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Vacancy> findByMinSalaryAndPosition(Long minSalary, Long positionId) {
        Session session = sessionFactory.openSession();
        try {
            return session.createQuery(
                            """
                                    select distinct v
                                    from Vacancy v
                                    join fetch v.company
                                    join fetch v.position
                                    join fetch v.minEducationLevel
                                    where (:positionId is null or v.position.id = :positionId)
                                      and (:minSalary is null or v.salary >= :minSalary)
                                    order by v.salary desc, v.id
                                    """, Vacancy.class)
                    .setParameter("positionId", positionId)
                    .setParameter("minSalary", minSalary)
                    .list();
        } finally {
            session.close();
        }
    }

    public Optional<Vacancy> findDetailedById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Vacancy vacancy = session.createQuery(
                            """
                                    select v
                                    from Vacancy v
                                    join fetch v.company
                                    join fetch v.position
                                    join fetch v.minEducationLevel
                                    where v.id = :id
                                    """, Vacancy.class)
                    .setParameter("id", id)
                    .uniqueResult();
            return Optional.ofNullable(vacancy);
        }
    }

    public List<Vacancy> findByFilters(Long companyId, Long positionId, Long minSalary, Long maxSalary,
                                       Long minExperienceMonths, Long educationLevelId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            """
                                    select distinct v
                                    from Vacancy v
                                    join fetch v.company
                                    join fetch v.position
                                    join fetch v.minEducationLevel
                                    where (:companyId is null or v.company.id = :companyId)
                                      and (:positionId is null or v.position.id = :positionId)
                                      and (:minSalary is null or v.salary >= :minSalary)
                                      and (:maxSalary is null or v.salary <= :maxSalary)
                                      and (:minExperienceMonths is null or v.minMonthsExperience >= :minExperienceMonths)
                                      and (:educationLevelId is null or v.minEducationLevel.id = :educationLevelId)
                                    order by v.salary desc, v.id
                                    """, Vacancy.class)
                    .setParameter("companyId", companyId)
                    .setParameter("positionId", positionId)
                    .setParameter("minSalary", minSalary)
                    .setParameter("maxSalary", maxSalary)
                    .setParameter("minExperienceMonths", minExperienceMonths)
                    .setParameter("educationLevelId", educationLevelId)
                    .list();
        }
    }

    public List<Vacancy> findMatchingForResume(Long resumeId) {
        Session session = sessionFactory.openSession();
        try {
            Resume resume = session.get(Resume.class, resumeId);
            if (resume == null || !resume.isActive()) {
                return Collections.emptyList();
            }

            return session.createQuery(
                            """
                                    select distinct v
                                    from Vacancy v, Resume resume
                                    join fetch v.company
                                    join fetch v.position
                                    join fetch v.minEducationLevel
                                    left join resume.educationRecords er
                                    left join resume.workExperiences we
                                    where resume.id = :resumeId
                                      and v.position.id = :positionId
                                      and v.salary >= :expectedSalary
                                      and v.minEducationLevel.id <= er.level.id
                                    order by v.salary desc, v.id
                                    """, Vacancy.class)
                    .setParameter("positionId", resume.getPosition().getId())
                    .setParameter("resumeId", resumeId)
                    .setParameter("expectedSalary", resume.getMinSalary())
                    .list();
        } finally {
            session.close();
        }
    }
}
