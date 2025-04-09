package ru.nsu.burym.crack_hash.worker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nsu.burym.crack_hash.model.generated.CrackHashWorkerResponse;
import ru.nsu.burym.crack_hash.worker.service.rabbit.ResultPublisherService;

import java.util.List;

@Service
public class ManagerService {

    private final ResultPublisherService resultPublisherService;

    @Autowired
    public ManagerService(final ResultPublisherService resultPublisherService) {
        this.resultPublisherService = resultPublisherService;
    }

    public void sendResult(final String requestId, final int partNumber, final List<String> words) {
        final CrackHashWorkerResponse crackHashWorkerResponse = new CrackHashWorkerResponse();
        crackHashWorkerResponse.setRequestId(requestId);
        crackHashWorkerResponse.setPartNumber(partNumber);
        crackHashWorkerResponse.setAnswers(new CrackHashWorkerResponse.Answers());
        crackHashWorkerResponse.getAnswers().getWords().addAll(words);

        resultPublisherService.publish(crackHashWorkerResponse);
    }
}
