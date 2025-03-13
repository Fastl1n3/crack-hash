package ru.nsu.burym.crack_hash.manager.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nsu.burym.crack_hash.manager.AppContext;
import ru.nsu.burym.crack_hash.manager.dto.StatusResponse;
import ru.nsu.burym.crack_hash.manager.model.Status;
import ru.nsu.burym.crack_hash.manager.model.TaskInfo;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class CrackHashService {
    private final AppContext appContext;

    private final WorkerService workerService;

    @Getter
    private final Map<UUID, TaskInfo> tasks;

    @Autowired
    public CrackHashService(final AppContext appContext, final WorkerService workerService) {
        this.appContext = appContext;
        this.workerService = workerService;
        tasks = new ConcurrentHashMap<>();
    }

    public UUID createTask(final String hash, final int maxLength) {
        checkPossibility();
        final UUID uuid = UUID.randomUUID();
        final TaskInfo taskInfo = new TaskInfo(hash, maxLength, uuid);
        tasks.put(uuid, taskInfo);

        workerService.sendTask(taskInfo);
        return uuid;
    }

    private void checkPossibility() {
        if (tasks.values().stream().filter(t -> t.getStatus() == Status.IN_PROGRESS)
                .count() == appContext.getMaxTasks()) {
            throw new IllegalStateException("too many tasks in progress");
        }
    }

    public void updateTask(final UUID uuid, final List<String> words) {
        tasks.computeIfPresent(uuid, (k, v) -> {
                    v.incrementFinishedWorkers();
                    v.getCrackedWords().addAll(words);
                    if (v.getStatus() == Status.IN_PROGRESS && v.getFinishedWorkersNum() == workerService.getNumWorkers()) {
                        v.setStatus(Status.READY);
                        log.info("task is ready: {}, {}", uuid, v.getCrackedWords());
                    }
                    return v;
                }
        );
    }

    public StatusResponse getStatus(final UUID requestId) {
        if (!tasks.containsKey(requestId)) {
            throw new IllegalArgumentException("requestId is undefined");
        }
        final TaskInfo taskInfo = tasks.get(requestId);
        return new StatusResponse(taskInfo.getStatus(), taskInfo.getCrackedWords());
    }
}
