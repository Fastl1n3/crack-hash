package ru.nsu.burym.crack_hash.manager;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "application")
@Getter
@Setter
public class AppContext {
    private String alphabet;
    private int timeout;
    private List<String> workerUrls;
    private int maxTasks;

}
