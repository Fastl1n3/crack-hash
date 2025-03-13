package ru.nsu.burym.crack_hash.manager.mapper;

import ru.nsu.burym.crack_hash.manager.model.TaskInfo;
import ru.nsu.burym.crack_hash.model.generated.CrackHashManagerRequest;

public class ManagerRequestMapper {
    public static CrackHashManagerRequest map(final TaskInfo taskInfo,
                                              final String alphabetStr,
                                              final int partNumber,
                                              final int partCount) {
        final CrackHashManagerRequest crackHashManagerRequest = new CrackHashManagerRequest();
        crackHashManagerRequest.setRequestId(taskInfo.getRequestId().toString());
        crackHashManagerRequest.setHash(taskInfo.getHash());
        crackHashManagerRequest.setMaxLength(taskInfo.getMaxLength());
        crackHashManagerRequest.setAlphabet(mapToAlphabet(alphabetStr));
        crackHashManagerRequest.setPartNumber(partNumber);
        crackHashManagerRequest.setPartCount(partCount);

        return crackHashManagerRequest;
    }

    private static CrackHashManagerRequest.Alphabet mapToAlphabet(final String alphabetStr) {
        final CrackHashManagerRequest.Alphabet alphabet = new CrackHashManagerRequest.Alphabet();
        alphabet.getSymbols().addAll(alphabetStr.chars().mapToObj(c -> String.valueOf((char) c)).toList());
        return alphabet;
    }
}
