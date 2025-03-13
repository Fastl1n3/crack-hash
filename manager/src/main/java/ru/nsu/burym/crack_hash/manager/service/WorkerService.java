package ru.nsu.burym.crack_hash.manager.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.nsu.burym.crack_hash.manager.AppContext;
import ru.nsu.burym.crack_hash.manager.mapper.ManagerRequestMapper;
import ru.nsu.burym.crack_hash.manager.model.TaskInfo;
import ru.nsu.burym.crack_hash.model.generated.CrackHashManagerRequest;

import java.util.List;

@Slf4j
@Service
public class WorkerService {
    private final AppContext appContext;

    private final RestTemplate restTemplate;

    @Autowired
    public WorkerService(final AppContext appContext) {
        this.appContext = appContext;
        restTemplate = new RestTemplate();
    }

    public void sendTask(final TaskInfo taskInfo) {
        final List<String> workerUrls = appContext.getWorkerUrls();
        final int workersNum = workerUrls.size();

        for (int i = 0; i < workersNum; i++) {
            final CrackHashManagerRequest crackHashManagerRequest =
                    ManagerRequestMapper.map(taskInfo, appContext.getAlphabet(), i, workersNum);

            final String workerUrl = workerUrls.get(i);
            final String response = restTemplate.postForObject(workerUrl + "/internal/api/worker/hash/crack/task",
                    crackHashManagerRequest, String.class);
            log.info("Response from worker {}: {}", i, response);
        }
    }

    public int getNumWorkers() {
        return appContext.getWorkerUrls().size();
    }
}
