package ru.nsu.burym.crack_hash.worker.service;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.paukov.combinatorics3.Generator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nsu.burym.crack_hash.model.generated.CrackHashManagerRequest;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

@Slf4j
@Service
public class TaskService {

    //static final int NUM_THREADS = Math.min(8, Runtime.getRuntime().availableProcessors());

    private final ManagerService managerService;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Autowired
    public TaskService(final ManagerService managerService) {
        this.managerService = managerService;
    }

    public void executeTask(final CrackHashManagerRequest crackHashManagerRequest) {
        final Runnable task = createCrackTask(crackHashManagerRequest);
        executorService.execute(task);
        log.info("task was added: {}", crackHashManagerRequest.getRequestId());
    }

    private Runnable createCrackTask(final CrackHashManagerRequest crackHashManagerRequest) {
        final List<String> alphabetSymbols = crackHashManagerRequest.getAlphabet().getSymbols();
        final int maxLen = crackHashManagerRequest.getMaxLength();
        final int partNumber = crackHashManagerRequest.getPartNumber();
        final int partCount = crackHashManagerRequest.getPartCount();
        final String hash = crackHashManagerRequest.getHash();
        final String requestId = crackHashManagerRequest.getRequestId();

        return () -> {
            final int alphabetSize = alphabetSymbols.size();
            final int totalSize = (int) (Math.pow(alphabetSize, maxLen + 1) - alphabetSize) / (alphabetSize - 1); // сумма геом. прогрессии
            final int partSize = totalSize / partCount;
            final int startIdx = partNumber * partSize;
            final int endIdx = partCount == partNumber - 1 ? totalSize : (partNumber + 1) * partSize;
            log.info(alphabetSymbols.toString());
            log.info("total {}", totalSize);
            log.info("partSize {}", partSize);
            log.info("startIdx {}", startIdx);
            log.info("endIdx {}", endIdx);
            log.info("maxLen {}", maxLen);

            Stream<List<String>> permutationsStream = Stream.empty();
            for (int len = 1; len <= maxLen; len++) {
                permutationsStream = Stream.concat(permutationsStream,
                        Generator.permutation(alphabetSymbols)
                                .withRepetitions(len)
                                .stream());
            }
            final List<String> words = permutationsStream
                    .skip(startIdx)
                    .limit(endIdx - startIdx)
                    .map(list -> String.join("", list))
                    .filter(word -> md5Hash(word).equals(hash))
                    .toList();
            log.info("worker {}: words was computed: {}", partNumber, words);
            managerService.sendResult(requestId, partNumber, words);
        };
    }

    private String md5Hash(final String word) {
        final byte[] wordBytes = word.getBytes(StandardCharsets.UTF_8);
        try {
            final MessageDigest md = MessageDigest.getInstance("MD5");
            final byte[] hashBytes = md.digest(wordBytes);

            final StringBuilder hexStr = new StringBuilder();
            for (final byte b : hashBytes) {
                hexStr.append(String.format("%02x", b));
            }
            return hexStr.toString();
        } catch (final NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @PreDestroy
    public void shutdown() {
        executorService.shutdownNow();
    }


}
