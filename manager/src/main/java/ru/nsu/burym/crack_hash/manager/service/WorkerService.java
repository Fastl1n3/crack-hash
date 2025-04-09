package ru.nsu.burym.crack_hash.manager.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.nsu.burym.crack_hash.manager.AppContext;
import ru.nsu.burym.crack_hash.manager.mapper.ManagerRequestMapper;
import ru.nsu.burym.crack_hash.manager.model.TaskInfo;
import ru.nsu.burym.crack_hash.manager.service.rabbit.TaskPublisherService;
import ru.nsu.burym.crack_hash.model.generated.CrackHashManagerRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class WorkerService {
    private final AppContext appContext;

    private final RestTemplate restTemplate;

    private final TaskPublisherService taskPublisherService;

    @Autowired
    public WorkerService(final AppContext appContext, final TaskPublisherService taskPublisherService) {
        this.appContext = appContext;
        this.taskPublisherService = taskPublisherService;
        restTemplate = new RestTemplate();
    }

    public void sendTask(final TaskInfo taskInfo) {
        final List<String> workerUrls = appContext.getWorkerUrls();
        final int workersNum = workerUrls.size();

        for (int i = 0; i < workersNum; i++) {
            final CrackHashManagerRequest crackHashManagerRequest =
                    ManagerRequestMapper.map(taskInfo, appContext.getAlphabet(), i, workersNum);
            taskPublisherService.publish(crackHashManagerRequest);
        }
    }

    public int getNumWorkers() {
        return appContext.getWorkerUrls().size();
    }

    public int getProcessedPercent(final UUID requestId, final int maxLen) {
        long processedWordsNum = 0;
        for (final String url : appContext.getWorkerUrls()) {
            final String path = url + "/internal/api/worker/hash/crack/task/percent?requestId={requestId}";
            long wordsNum = 0;
            try {
                wordsNum = Optional.ofNullable(restTemplate.getForObject(path, Long.class, requestId))
                        .orElse(0L);
            } catch (final Exception e) {
                log.error("error with url {}:", url, e);
            }
            processedWordsNum += wordsNum;
        }
        final int alphabetSize = appContext.getAlphabet().length();
        final long totalWords = (long) (Math.pow(alphabetSize, maxLen + 1) - alphabetSize) / (alphabetSize - 1);

        log.info("processedWordsNum {}", processedWordsNum);
        log.info("totalWords {}", totalWords);
        final long percent = processedWordsNum * 100 / totalWords;
        return percent > 100 ? 100 : (int) percent;
    }
}
