package ru.nsu.burym.crack_hash.manager.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.burym.crack_hash.manager.service.CrackHashService;
import ru.nsu.burym.crack_hash.model.generated.CrackHashWorkerResponse;

import java.util.UUID;

@Slf4j
@RestController
public class ResultController {

    private final CrackHashService crackHashService;

    @Autowired
    public ResultController(final CrackHashService crackHashService) {
        this.crackHashService = crackHashService;
    }

    @PostMapping("/internal/api/manager/hash/crack/request")
    public ResponseEntity<String> updateWords(@RequestBody final CrackHashWorkerResponse crackHashWorkerResponse) {
        log.info("Words from worker {}: {}",
                crackHashWorkerResponse.getPartNumber(),
                crackHashWorkerResponse.getAnswers().getWords());
        crackHashService.updateTask(UUID.fromString(crackHashWorkerResponse.getRequestId()),
                crackHashWorkerResponse.getAnswers().getWords());
        return ResponseEntity.ok().build();
    }
}
