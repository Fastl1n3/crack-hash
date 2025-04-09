package ru.nsu.burym.crack_hash.manager.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.burym.crack_hash.manager.AppContext;
import ru.nsu.burym.crack_hash.manager.dto.StatusResponse;
import ru.nsu.burym.crack_hash.manager.model.Status;
import ru.nsu.burym.crack_hash.manager.model.TaskInfo;
import ru.nsu.burym.crack_hash.manager.repository.TaskRepository;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class CrackHashService {
    private final AppContext appContext;

    private final WorkerService workerService;

    private final TaskRepository taskRepository;

    @Autowired
    public CrackHashService(final AppContext appContext,
                            final WorkerService workerService,
                            final TaskRepository taskRepository) {
        this.appContext = appContext;
        this.workerService = workerService;
        this.taskRepository = taskRepository;
    }

    public UUID createTask(final String hash, final int maxLength) {
        checkPossibility();
        final UUID uuid = UUID.randomUUID();
        final TaskInfo taskInfo = new TaskInfo(hash, maxLength, uuid);

        taskRepository.save(taskInfo);
        workerService.sendTask(taskInfo);
        return uuid;
    }

    private void checkPossibility() {
        final List<TaskInfo> tasks = taskRepository.findAll();
        if (tasks.stream().filter(t -> t.getStatus() == Status.IN_PROGRESS)
                .count() == appContext.getMaxTasks()) {
            throw new IllegalStateException("too many tasks in progress");
        }
    }

    public StatusResponse getStatus(final UUID requestId) {
        final TaskInfo taskInfo = taskRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("requestId is undefined"));
        final int percent;
        if (taskInfo.getStatus() == Status.READY) {
            percent = 100;
        } else {
            percent = workerService.getProcessedPercent(taskInfo.getRequestId(), taskInfo.getMaxLength());
            log.info("PERCENT {}", percent);
        }

        return new StatusResponse(taskInfo.getStatus(), taskInfo.getCrackedWords(), percent);
    }

    @Transactional
    public void updateTask(final UUID uuid, final List<String> words) {
        final TaskInfo taskInfo = taskRepository.findById(uuid).orElseThrow();
        if (taskInfo.getStatus() == Status.READY) {
            return;
        }
        taskInfo.incrementFinishedWorkers();
        taskInfo.getCrackedWords().addAll(words);
        if (taskInfo.getStatus() == Status.IN_PROGRESS &&
                taskInfo.getFinishedWorkersNum() == workerService.getNumWorkers()) {
            taskInfo.setStatus(Status.READY);
            log.info("task is ready: {}, {}", uuid, taskInfo.getCrackedWords());
        }
        taskRepository.save(taskInfo);
    }

    @Transactional
    public List<TaskInfo> getTasks() {
        return taskRepository.findAll();
    }
}
