package ru.nsu.burym.crack_hash.manager.dto;

import ru.nsu.burym.crack_hash.manager.model.Status;

import java.util.List;

public record StatusResponse(Status status, List<String> data, int progress) {
}
