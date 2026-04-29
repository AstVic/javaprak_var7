package ru.javaprak.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.javaprak.service.RecruitmentService;

@Controller
public class ResponseController {

    private final RecruitmentService service;

    public ResponseController(RecruitmentService service) {
        this.service = service;
    }

    @PostMapping("/responses")
    public String create(@RequestParam Long resumeId,
                         @RequestParam Long vacancyId,
                         RedirectAttributes redirectAttributes) {
        try {
            service.createResponse(resumeId, vacancyId);
            redirectAttributes.addFlashAttribute("message", "Отклик отправлен");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/vacancies/" + vacancyId;
    }
}
