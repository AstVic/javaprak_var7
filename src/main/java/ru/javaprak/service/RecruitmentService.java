package ru.javaprak.service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.javaprak.dao.ReferenceDao;
import ru.javaprak.dao.ResponseDao;
import ru.javaprak.dao.ResumeDao;
import ru.javaprak.dao.VacancyDao;
import ru.javaprak.entity.Applicant;
import ru.javaprak.entity.Company;
import ru.javaprak.entity.Education;
import ru.javaprak.entity.EducationLevel;
import ru.javaprak.entity.Position;
import ru.javaprak.entity.Response;
import ru.javaprak.entity.ResponseStatus;
import ru.javaprak.entity.Resume;
import ru.javaprak.entity.Vacancy;
import ru.javaprak.entity.WorkExperience;
import ru.javaprak.util.HibernateUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class RecruitmentService {

    private final ResumeDao resumeDao;
    private final VacancyDao vacancyDao;
    private final ResponseDao responseDao;
    private final ReferenceDao referenceDao;

    public RecruitmentService(ResumeDao resumeDao, VacancyDao vacancyDao, ResponseDao responseDao, ReferenceDao referenceDao) {
        this.resumeDao = resumeDao;
        this.vacancyDao = vacancyDao;
        this.responseDao = responseDao;
        this.referenceDao = referenceDao;
        initData();
    }

    private void initData() {
        try {
            if (!referenceDao.findPositions().isEmpty()) {
                return;
            }
        } catch (Exception e) {
            return;
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            Position javaDeveloper = new Position(1L, "Java-разработчик");
            Position backendDeveloper = new Position(2L, "Backend-разработчик");
            Position systemAnalyst = new Position(3L, "Системный аналитик");
            Position dataAnalyst = new Position(4L, "Data Analyst");
            Position devOps = new Position(5L, "DevOps-инженер");
            Position qaEngineer = new Position(6L, "QA-инженер");
            session.persist(javaDeveloper);
            session.persist(backendDeveloper);
            session.persist(systemAnalyst);
            session.persist(dataAnalyst);
            session.persist(devOps);
            session.persist(qaEngineer);

            EducationLevel secondary = new EducationLevel(1L, "Среднее");
            EducationLevel vocational = new EducationLevel(2L, "Среднее профессиональное");
            EducationLevel bachelor = new EducationLevel(3L, "Бакалавриат");
            EducationLevel master = new EducationLevel(4L, "Магистратура");
            EducationLevel phd = new EducationLevel(5L, "Аспирантура");
            session.persist(secondary);
            session.persist(vocational);
            session.persist(bachelor);
            session.persist(master);
            session.persist(phd);

            Company yandex = new Company(1L, "Яндекс", "+7 (495) 739-70-00; hr@yandex-team.ru; г. Москва, ул. Льва Толстого, 16");
            session.persist(yandex);

            Applicant ivanov = new Applicant(1L, "Иванов Иван Сергеевич",
                    "+7 (495) 123-45-67; ivanov.is@mail.ru",
                    LocalDate.of(1999, 4, 12),
                    "г. Москва, ул. Тверская, д. 10, кв. 15",
                    true);
            session.persist(ivanov);

            Education education1 = new Education(1L, bachelor, "МГУ имени М.В. Ломоносова",
                    "Прикладная математика и информатика",
                    LocalDate.of(2017, 9, 1), LocalDate.of(2021, 6, 30));
            Education education2 = new Education(2L, master, "МГУ имени М.В. Ломоносова",
                    "Программная инженерия",
                    LocalDate.of(2021, 9, 1), LocalDate.of(2023, 6, 30));
            session.persist(education1);
            session.persist(education2);

            WorkExperience workExperience1 = new WorkExperience(1L, "АО «ТехИнтеграция»", backendDeveloper,
                    LocalDate.of(2021, 9, 1), LocalDate.of(2023, 1, 31), 150000L);
            WorkExperience workExperience2 = new WorkExperience(2L, "ООО «СеверСофт»", javaDeveloper,
                    LocalDate.of(2023, 2, 1), null, 210000L);
            session.persist(workExperience1);
            session.persist(workExperience2);

            Resume resume1 = new Resume(1L, ivanov, javaDeveloper, 180000L, true);
            resume1.getEducationRecords().add(education1);
            resume1.getEducationRecords().add(education2);
            resume1.getWorkExperiences().add(workExperience1);
            resume1.getWorkExperiences().add(workExperience2);
            session.persist(resume1);

            Vacancy vacancy1 = new Vacancy(1L, yandex, javaDeveloper, 250000L, 24L, bachelor);
            session.persist(vacancy1);

            Response response1 = new Response(1L, resume1, vacancy1, ResponseStatus.recommended,
                    LocalDate.of(2026, 2, 1));
            session.persist(response1);

            tx.commit();
        } catch (Exception e) {
            // If initialization fails, keep running with an empty database.
        }
    }

    public List<Resume> findResumes() {
        return resumeDao.findAllDetailed();
    }

    public Optional<Resume> findResume(Long id) {
        return resumeDao.findDetailedById(id);
    }

    public List<Resume> searchResumes(Long positionId, Long minSalary, Long maxSalary,
                                      Long educationLevelId, String companyName) {
        validateSalaryRange(minSalary, maxSalary);
        return resumeDao.findActiveByFilters(positionId, minSalary, maxSalary, educationLevelId, companyName);
    }

    public List<Vacancy> searchVacancies(Long companyId, Long positionId, Long minSalary, Long maxSalary,
                                         Long minExperienceMonths, Long educationLevelId) {
        validateSalaryRange(minSalary, maxSalary);
        validateNonNegative(minExperienceMonths, "Минимальный стаж не может быть отрицательным");
        return vacancyDao.findByFilters(companyId, positionId, minSalary, maxSalary, minExperienceMonths, educationLevelId);
    }

    public Optional<Vacancy> findVacancy(Long id) {
        return vacancyDao.findDetailedById(id);
    }

    public List<Vacancy> findMatchingVacancies(Long resumeId) {
        return vacancyDao.findMatchingForResume(resumeId);
    }

    public List<Resume> findMatchingResumes(Long vacancyId) {
        return resumeDao.findMatchingForVacancy(vacancyId);
    }

    public Resume createResume(Long applicantId, Long positionId, Long minSalary, boolean active) {
        validateResume(applicantId, positionId, minSalary);
        return resumeDao.create(applicantId, positionId, minSalary, active);
    }

    public void updateResume(Long id, Long applicantId, Long positionId, Long minSalary, boolean active) {
        validateResume(applicantId, positionId, minSalary);
        resumeDao.update(id, applicantId, positionId, minSalary, active);
    }

    public void deleteResume(Long id) {
        resumeDao.delete(id);
    }

    public Response createResponse(Long resumeId, Long vacancyId) {
        if (resumeId == null || vacancyId == null) {
            throw new IllegalArgumentException("Нужно выбрать резюме и вакансию");
        }
        if (resumeDao.findById(resumeId).isEmpty()) {
            throw new IllegalArgumentException("Резюме не найдено");
        }
        if (vacancyDao.findDetailedById(vacancyId).isEmpty()) {
            throw new IllegalArgumentException("Вакансия не найдена");
        }
        if (responseDao.existsByResumeAndVacancy(resumeId, vacancyId)) {
            throw new IllegalArgumentException("Отклик для этой пары резюме и вакансии уже существует");
        }
        return responseDao.create(resumeId, vacancyId);
    }

    public List<Position> findPositions() {
        return referenceDao.findPositions();
    }

    public List<EducationLevel> findEducationLevels() {
        return referenceDao.findEducationLevels();
    }

    public List<Company> findCompanies() {
        return referenceDao.findCompanies();
    }

    public List<Applicant> findApplicants() {
        return referenceDao.findApplicants();
    }

    private void validateResume(Long applicantId, Long positionId, Long minSalary) {
        if (applicantId == null) {
            throw new IllegalArgumentException("Нужно выбрать соискателя");
        }
        if (positionId == null) {
            throw new IllegalArgumentException("Нужно выбрать должность");
        }
        validateNonNegative(minSalary, "Желаемая зарплата не может быть отрицательной");
    }

    private void validateSalaryRange(Long minSalary, Long maxSalary) {
        validateNonNegative(minSalary, "Минимальная зарплата не может быть отрицательной");
        validateNonNegative(maxSalary, "Максимальная зарплата не может быть отрицательной");
        if (minSalary != null && maxSalary != null && minSalary > maxSalary) {
            throw new IllegalArgumentException("Минимальная зарплата не может превышать максимальную");
        }
    }

    private void validateNonNegative(Long value, String message) {
        if (value != null && value < 0) {
            throw new IllegalArgumentException(message);
        }
    }
}
