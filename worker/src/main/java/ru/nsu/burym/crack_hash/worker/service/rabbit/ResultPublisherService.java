package ru.nsu.burym.crack_hash.worker.service.rabbit;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.nsu.burym.crack_hash.model.generated.CrackHashWorkerResponse;

@Service
public class ResultPublisherService {

    private final RabbitTemplate rabbitTemplate;

    private final String exchange;

    private final String key;

    @Autowired
    public ResultPublisherService(final RabbitTemplate rabbitTemplate,
                                  @Value("${rabbitmq.result.exchange}") final String exchange,
                                  @Value("${rabbitmq.result.key}") final String key) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
        this.key = key;
    }

    public void publish(final CrackHashWorkerResponse crackHashWorkerResponse) {
        rabbitTemplate.convertAndSend(exchange, key, crackHashWorkerResponse);
    }
}
