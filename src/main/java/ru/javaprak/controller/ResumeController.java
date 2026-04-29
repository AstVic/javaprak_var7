package ru.javaprak.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.javaprak.entity.Resume;
import ru.javaprak.service.RecruitmentService;

@Controller
public class ResumeController {

    private final RecruitmentService service;

    public ResumeController(RecruitmentService service) {
        this.service = service;
    }

    @GetMapping("/resumes")
    public String resumes(Model model) {
        model.addAttribute("resumes", service.findResumes());
        return "resumes";
    }

    @GetMapping("/resumes/{id}")
    public String resume(@PathVariable Long id, Model model) {
        Resume resume = service.findResume(id)
                .orElseThrow(() -> new IllegalArgumentException("Резюме не найдено"));
        model.addAttribute("resume", resume);
        return "resume";
    }

    @GetMapping("/resumes/new")
    public String newResume(Model model) {
        fillReferences(model);
        model.addAttribute("mode", "create");
        return "resume-form";
    }

    @PostMapping("/resumes")
    public String createResume(@RequestParam Long applicantId,
                               @RequestParam Long positionId,
                               @RequestParam Long minSalary,
                               @RequestParam(defaultValue = "false") boolean active,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        try {
            Resume resume = service.createResume(applicantId, positionId, minSalary, active);
            redirectAttributes.addFlashAttribute("message", "Резюме создано");
            return "redirect:/resumes/" + resume.getId();
        } catch (IllegalArgumentException e) {
            fillReferences(model);
            model.addAttribute("mode", "create");
            model.addAttribute("error", e.getMessage());
            return "resume-form";
        }
    }

    @GetMapping("/resumes/{id}/edit")
    public String editResume(@PathVariable Long id, Model model) {
        Resume resume = service.findResume(id)
                .orElseThrow(() -> new IllegalArgumentException("Резюме не найдено"));
        fillReferences(model);
        model.addAttribute("resume", resume);
        model.addAttribute("mode", "edit");
        return "resume-form";
    }

    @PostMapping("/resumes/{id}")
    public String updateResume(@PathVariable Long id,
                               @RequestParam Long applicantId,
                               @RequestParam Long positionId,
                               @RequestParam Long minSalary,
                               @RequestParam(defaultValue = "false") boolean active,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        try {
            service.updateResume(id, applicantId, positionId, minSalary, active);
            redirectAttributes.addFlashAttribute("message", "Резюме обновлено");
            return "redirect:/resumes/" + id;
        } catch (IllegalArgumentException e) {
            fillReferences(model);
            model.addAttribute("resume", service.findResume(id).orElse(null));
            model.addAttribute("mode", "edit");
            model.addAttribute("error", e.getMessage());
            return "resume-form";
        }
    }

    @PostMapping("/resumes/{id}/delete")
    public String deleteResume(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        service.deleteResume(id);
        redirectAttributes.addFlashAttribute("message", "Резюме удалено");
        return "redirect:/resumes";
    }

    private void fillReferences(Model model) {
        model.addAttribute("applicants", service.findApplicants());
        model.addAttribute("positions", service.findPositions());
    }
}
