package ru.nsu.burym.crack_hash.manager.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.burym.crack_hash.manager.dto.CrackHashRequest;
import ru.nsu.burym.crack_hash.manager.dto.CrackHashResponse;
import ru.nsu.burym.crack_hash.manager.dto.StatusResponse;
import ru.nsu.burym.crack_hash.manager.service.CrackHashService;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/hash")
public class CrackHashController {
    
    private final CrackHashService crackHashService;

    @Autowired
    public CrackHashController(final CrackHashService crackHashService) {
        this.crackHashService = crackHashService;
    }

    @PostMapping("/crack")
    public ResponseEntity<CrackHashResponse> crackHash(@RequestBody final CrackHashRequest crackHashRequest) {
        try {
            final UUID requestId = crackHashService.createTask(crackHashRequest.hash(), crackHashRequest.maxLength());
            log.info("New request {}", requestId);
            return ResponseEntity.ok(new CrackHashResponse(requestId));
        } catch (final IllegalStateException e) {
            log.warn("request wasn't accepted: {}", e.getMessage());
            return ResponseEntity.status(429).build();
        }

    }

    @GetMapping("/status")
    public ResponseEntity<StatusResponse> getStatus(@RequestParam final UUID requestId) {
        try {
            return ResponseEntity.ok(crackHashService.getStatus(requestId));
        } catch (final IllegalArgumentException e) {
            log.info("Undef requestId {}", requestId);
            return ResponseEntity.badRequest().build();
        }
    }
}
