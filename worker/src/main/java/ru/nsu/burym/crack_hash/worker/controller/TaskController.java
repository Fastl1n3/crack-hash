package ru.nsu.burym.crack_hash.worker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.burym.crack_hash.model.generated.CrackHashManagerRequest;
import ru.nsu.burym.crack_hash.worker.service.TaskService;

@RestController
@RequestMapping("/internal/api/worker/hash/crack/task")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(final TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping()
    public ResponseEntity<String> executeTask(@RequestBody final CrackHashManagerRequest crackHashManagerRequest) {
        taskService.executeTask(crackHashManagerRequest); //todo обернуть исключение
        return ResponseEntity.ok("Task was accepted.");
    }

    @GetMapping("/percent")
    public ResponseEntity<Long> getPercent(@RequestParam final String requestId) {
        return ResponseEntity.ok(taskService.getProcessedWordsNum(requestId));
    }
}
