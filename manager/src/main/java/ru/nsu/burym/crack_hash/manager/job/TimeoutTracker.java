package ru.nsu.burym.crack_hash.manager.job;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.nsu.burym.crack_hash.manager.AppContext;
import ru.nsu.burym.crack_hash.manager.model.Status;
import ru.nsu.burym.crack_hash.manager.model.TaskInfo;
import ru.nsu.burym.crack_hash.manager.service.CrackHashService;

import java.util.Collection;

@Slf4j
@Component
public class TimeoutTracker {

    private final AppContext appContext;
    private final CrackHashService crackHashService;

    @Autowired
    public TimeoutTracker(final AppContext appContext, final CrackHashService crackHashService) {
        this.appContext = appContext;
        this.crackHashService = crackHashService;
    }


    @Scheduled(fixedDelayString = "${application.scheduler-delay}")
    public void checkTimeout() {
        final Collection<TaskInfo> taskInfos = crackHashService.getTasks();
        for (final TaskInfo taskInfo : taskInfos) {
            final long spentTimeMs = System.currentTimeMillis() - taskInfo.getStartTime();
            if (taskInfo.getStatus() == Status.IN_PROGRESS && spentTimeMs > appContext.getTimeout()) {
                if (taskInfo.getFinishedWorkersNum() > 0) {
                    taskInfo.setStatus(Status.PARTIAL_READY);
                    log.info("Timeout: task {} was marked PARTIAL_READY, num of finished workers {}",
                            taskInfo.getRequestId(), taskInfo.getFinishedWorkersNum());
                } else {
                    taskInfo.setStatus(Status.ERROR);
                    log.info("Timeout: task {} was marked ERROR", taskInfo.getRequestId());
                }
            }
        }
    }
}
