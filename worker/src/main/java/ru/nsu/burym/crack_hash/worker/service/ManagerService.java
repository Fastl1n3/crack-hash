package ru.nsu.burym.crack_hash.worker.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.nsu.burym.crack_hash.model.generated.CrackHashWorkerResponse;

import java.util.List;

@Service
public class ManagerService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${application.manager-url}")
    private String managerUrl;

    public void sendResult(final String requestId, final int partNumber, final List<String> words) {
        final CrackHashWorkerResponse crackHashWorkerResponse = new CrackHashWorkerResponse();
        crackHashWorkerResponse.setRequestId(requestId);
        crackHashWorkerResponse.setPartNumber(partNumber);
        crackHashWorkerResponse.setAnswers(new CrackHashWorkerResponse.Answers());
        crackHashWorkerResponse.getAnswers().getWords().addAll(words);
        restTemplate.postForObject( managerUrl + "/internal/api/manager/hash/crack/request",
                crackHashWorkerResponse, String.class);
    }
}
