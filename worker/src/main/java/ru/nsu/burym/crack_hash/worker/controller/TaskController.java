package ru.nsu.burym.crack_hash.worker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.burym.crack_hash.model.generated.CrackHashManagerRequest;
import ru.nsu.burym.crack_hash.worker.service.TaskService;

@RestController
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(final TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/internal/api/worker/hash/crack/task")
    public ResponseEntity<String> executeTask(@RequestBody final CrackHashManagerRequest crackHashManagerRequest) {
        taskService.executeTask(crackHashManagerRequest); //todo обернуть исключение
        return ResponseEntity.ok("Task was accepted.");
    }
}
