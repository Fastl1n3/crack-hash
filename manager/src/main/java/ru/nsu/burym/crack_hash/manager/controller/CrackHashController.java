package ru.nsu.burym.crack_hash.manager.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.nsu.burym.crack_hash.manager.dto.CrackHashRequest;
import ru.nsu.burym.crack_hash.manager.dto.StatusResponse;
import ru.nsu.burym.crack_hash.manager.service.CrackHashService;

import java.util.UUID;

@Slf4j
@Controller
@RequestMapping("/hash")
public class CrackHashController {

    private final CrackHashService crackHashService;

    @Autowired
    public CrackHashController(final CrackHashService crackHashService) {
        this.crackHashService = crackHashService;
    }

    @GetMapping("/crack")
    public String showCrackForm(final Model model) {
        model.addAttribute("crackHashRequest", new CrackHashRequest("", 0));
        return "crack-form";
    }

    @PostMapping("/crack")
    public String crackHash(@ModelAttribute final CrackHashRequest crackHashRequest, final Model model) {
        try {
            final UUID requestId = crackHashService.createTask(crackHashRequest.hash(), crackHashRequest.maxLength());
            log.info("New request {}", requestId);
            model.addAttribute("requestId", requestId);
            return "crack-result";
        } catch (final IllegalStateException e) {
            log.warn("request wasn't accepted: {}", e.getMessage());
            model.addAttribute("error", "Request wasn't accepted: " + e.getMessage());
            return "crack-form";
        }
    }

    @GetMapping("/status")
    public String getStatus(@RequestParam final UUID requestId, final Model model) {
        try {
            final StatusResponse statusResponse = crackHashService.getStatus(requestId);
            model.addAttribute("statusResponse", statusResponse);
            return "status";
        } catch (final IllegalArgumentException e) {
            log.warn("Undef requestId {}", requestId);
            model.addAttribute("error", "Undefined requestId: " + requestId);
            return "status";
        }
    }
}
