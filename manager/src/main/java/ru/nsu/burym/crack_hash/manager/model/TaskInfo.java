package ru.nsu.burym.crack_hash.manager.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class TaskInfo {
    private final String hash;
    private final int maxLength;
    private final UUID requestId;
    private final List<String> crackedWords;
    private final long startTime;

    @Setter
    private Status status;
    private int finishedWorkersNum;

    public TaskInfo(final String hash, final int maxLength, final UUID requestId) {
        this.hash = hash;
        this.maxLength = maxLength;
        this.requestId = requestId;
        crackedWords = new ArrayList<>();
        status = Status.IN_PROGRESS;
        finishedWorkersNum = 0;
        startTime = System.currentTimeMillis();
    }

    public void incrementFinishedWorkers() {
        finishedWorkersNum++;
    }
}
