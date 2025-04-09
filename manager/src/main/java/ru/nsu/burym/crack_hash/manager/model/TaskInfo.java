package ru.nsu.burym.crack_hash.manager.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Document
public class TaskInfo {
    @Id
    private UUID requestId;

    private String hash;
    private int maxLength;
    private List<String> crackedWords;
    private long startTime;

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
