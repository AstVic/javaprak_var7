package ru.javaprak.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import ru.javaprak.entity.*;

import java.util.List;
import java.util.Optional;

public class ResumeDao {

    private final SessionFactory sessionFactory;

    public ResumeDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Optional<Resume> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.get(Resume.class, id));
        }
    }

    public Optional<Resume> findDetailedById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Resume resume = session.createQuery(
                            """
                                    select distinct r
                                    from Resume r
                                    join fetch r.applicant
                                    join fetch r.position
                                    left join fetch r.educationRecords er
                                    left join fetch er.level
                                    left join fetch r.workExperiences we
                                    left join fetch we.position
                                    where r.id = :id
                                    """, Resume.class)
                    .setParameter("id", id)
                    .uniqueResult();
            return Optional.ofNullable(resume);
        }
    }

    public List<Resume> findAllDetailed() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            """
                                    select distinct r
                                    from Resume r
                                    join fetch r.applicant
                                    join fetch r.position
                                    left join fetch r.educationRecords er
                                    left join fetch er.level
                                    order by r.id
                                    """, Resume.class)
                    .list();
        }
    }

    public List<Resume> findActiveByFilters(Long positionId, Long minSalary, Long maxSalary) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            """
                                    select distinct r
                                    from Resume r
                                    join fetch r.applicant
                                    join fetch r.position
                                    left join r.educationRecords er
                                    left join r.workExperiences we
                                    where r.active = true
                                      and (:positionId is null or r.position.id = :positionId)
                                      and (:minSalary is null or r.minSalary >= :minSalary)
                                      and (:maxSalary is null or r.minSalary <= :maxSalary)
                                      and (:educationLevelId is null or er.level.id = :educationLevelId)
                                      and (:companyName is null or lower(we.companyName) like lower(concat('%', :companyName, '%')))
                                    order by r.id
                                    """, Resume.class)
                    .setParameter("positionId", positionId)
                    .setParameter("minSalary", minSalary)
                    .setParameter("maxSalary", maxSalary)
                    .setParameter("educationLevelId", null)
                    .setParameter("companyName", null)
                    .list();
        }
    }

    public List<Resume> findActiveByFilters(Long positionId, Long minSalary, Long maxSalary,
                                            Long educationLevelId, String companyName) {
        try (Session session = sessionFactory.openSession()) {
            StringBuilder hql = new StringBuilder("""
                    select distinct r
                    from Resume r
                    join fetch r.applicant
                    join fetch r.position
                    left join r.educationRecords er
                    left join r.workExperiences we
                    where r.active = true
                    """);
            if (positionId != null) {
                hql.append(" and r.position.id = :positionId");
            }
            if (minSalary != null) {
                hql.append(" and r.minSalary >= :minSalary");
            }
            if (maxSalary != null) {
                hql.append(" and r.minSalary <= :maxSalary");
            }
            if (educationLevelId != null) {
                hql.append(" and er.level.id = :educationLevelId");
            }
            if (blankToNull(companyName) != null) {
                hql.append(" and lower(we.companyName) like lower(:companyName)");
            }
            hql.append(" order by r.id");

            var query = session.createQuery(hql.toString(), Resume.class);
            if (positionId != null) {
                query.setParameter("positionId", positionId);
            }
            if (minSalary != null) {
                query.setParameter("minSalary", minSalary);
            }
            if (maxSalary != null) {
                query.setParameter("maxSalary", maxSalary);
            }
            if (educationLevelId != null) {
                query.setParameter("educationLevelId", educationLevelId);
            }
            if (blankToNull(companyName) != null) {
                query.setParameter("companyName", "%" + companyName + "%");
            }
            return query.list();
        }
    }

    public List<Resume> findMatchingForVacancy(Long vacancyId) {
        try (Session session = sessionFactory.openSession()) {
            Vacancy vacancy = session.get(Vacancy.class, vacancyId);
            if (vacancy == null) {
                return List.of();
            }
            return session.createQuery(
                            """
                                    select distinct r
                                    from Resume r
                                    join fetch r.applicant
                                    join fetch r.position
                                    left join r.educationRecords er
                                    left join r.workExperiences we
                                    where r.active = true
                                      and r.position.id = :positionId
                                      and r.minSalary <= :salary
                                      and er.level.id >= :levelId
                                    order by r.id
                                    """, Resume.class)
                    .setParameter("positionId", vacancy.getPosition().getId())
                    .setParameter("salary", vacancy.getSalary())
                    .setParameter("levelId", vacancy.getMinEducationLevel().getId())
                    .list();
        }
    }

    public Resume create(Long applicantId, Long positionId, Long minSalary, boolean active) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Long nextId = nextId(session);
            Resume resume = new Resume(
                    nextId,
                    session.get(Applicant.class, applicantId),
                    session.get(Position.class, positionId),
                    minSalary,
                    active
            );
            session.persist(resume);
            tx.commit();
            return resume;
        }
    }

    public void update(Long id, Long applicantId, Long positionId, Long minSalary, boolean active) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.createMutationQuery(
                            """
                                    update Resume r
                                    set r.applicant = :applicant,
                                        r.position = :position,
                                        r.minSalary = :minSalary,
                                        r.active = :active
                                    where r.id = :id
                                    """)
                    .setParameter("applicant", session.get(Applicant.class, applicantId))
                    .setParameter("position", session.get(Position.class, positionId))
                    .setParameter("minSalary", minSalary)
                    .setParameter("active", active)
                    .setParameter("id", id)
                    .executeUpdate();
            tx.commit();
        }
    }

    public void delete(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Resume resume = session.get(Resume.class, id);
            if (resume != null) {
                session.remove(resume);
            }
            tx.commit();
        }
    }

    private Long nextId(Session session) {
        Long maxId = session.createQuery("select coalesce(max(r.id), 0) from Resume r", Long.class).uniqueResult();
        return maxId + 1;
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value;
    }
}
