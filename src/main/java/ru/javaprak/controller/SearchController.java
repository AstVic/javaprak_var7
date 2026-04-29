package ru.javaprak.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.javaprak.service.RecruitmentService;

@Controller
public class SearchController {

    private final RecruitmentService service;

    public SearchController(RecruitmentService service) {
        this.service = service;
    }

    @GetMapping("/search")
    public String search(@RequestParam(defaultValue = "vacancies") String mode,
                         @RequestParam(required = false) Long companyId,
                         @RequestParam(required = false) Long positionId,
                         @RequestParam(required = false) Long minSalary,
                         @RequestParam(required = false) Long maxSalary,
                         @RequestParam(required = false) Long minExperienceMonths,
                         @RequestParam(required = false) Long educationLevelId,
                         @RequestParam(required = false) String companyName,
                         Model model) {
        fillReferences(model);
        model.addAttribute("mode", mode);
        try {
            if ("resumes".equals(mode)) {
                model.addAttribute("resumes", service.searchResumes(
                        positionId, minSalary, maxSalary, educationLevelId, companyName));
            } else {
                model.addAttribute("vacancies", service.searchVacancies(
                        companyId, positionId, minSalary, maxSalary, minExperienceMonths, educationLevelId));
            }
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "search";
    }

    private void fillReferences(Model model) {
        model.addAttribute("companies", service.findCompanies());
        model.addAttribute("positions", service.findPositions());
        model.addAttribute("educationLevels", service.findEducationLevels());
    }
}
