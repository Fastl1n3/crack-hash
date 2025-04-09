package ru.nsu.burym.crack_hash.manager.service.rabbit;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.nsu.burym.crack_hash.model.generated.CrackHashManagerRequest;

@Service
public class TaskPublisherService {

    private final RabbitTemplate rabbitTemplate;

    private final String exchange;

    private final String key;

    @Autowired
    public TaskPublisherService(final RabbitTemplate rabbitTemplate,
                                @Value("${rabbitmq.tasks.exchange}") final String exchange,
                                @Value("${rabbitmq.tasks.key}") final String key) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
        this.key = key;
    }

    public void publish(final CrackHashManagerRequest crackHashManagerRequest) {
        rabbitTemplate.convertAndSend(exchange, key, crackHashManagerRequest);
    }
}
