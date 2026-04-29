package ru.javaprak.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.javaprak.entity.Vacancy;
import ru.javaprak.service.RecruitmentService;

@Controller
public class VacancyController {

    private final RecruitmentService service;

    public VacancyController(RecruitmentService service) {
        this.service = service;
    }

    @GetMapping("/vacancies/{id}")
    public String vacancy(@PathVariable Long id, Model model) {
        Vacancy vacancy = service.findVacancy(id)
                .orElseThrow(() -> new IllegalArgumentException("Вакансия не найдена"));
        model.addAttribute("vacancy", vacancy);
        model.addAttribute("resumes", service.findResumes());
        return "vacancy";
    }

    @GetMapping("/resumes/{id}/matches")
    public String matchingVacancies(@PathVariable Long id, Model model) {
        model.addAttribute("resume", service.findResume(id).orElseThrow(() -> new IllegalArgumentException("Резюме не найдено")));
        model.addAttribute("vacancies", service.findMatchingVacancies(id));
        return "resume-matches";
    }

    @GetMapping("/vacancies/{id}/matches")
    public String matchingResumes(@PathVariable Long id, Model model) {
        model.addAttribute("vacancy", service.findVacancy(id).orElseThrow(() -> new IllegalArgumentException("Вакансия не найдена")));
        model.addAttribute("resumes", service.findMatchingResumes(id));
        return "vacancy-matches";
    }
}
