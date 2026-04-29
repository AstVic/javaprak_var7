package ru.javaprak.service;

import ru.javaprak.dao.ReferenceDao;
import ru.javaprak.dao.ResponseDao;
import ru.javaprak.dao.ResumeDao;
import ru.javaprak.dao.VacancyDao;
import ru.javaprak.entity.Applicant;
import ru.javaprak.entity.Company;
import ru.javaprak.entity.EducationLevel;
import ru.javaprak.entity.Position;
import ru.javaprak.entity.Response;
import ru.javaprak.entity.Resume;
import ru.javaprak.entity.Vacancy;

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
