package ru.nsu.burym.crack_hash.manager.service.rabbit;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nsu.burym.crack_hash.manager.service.CrackHashService;
import ru.nsu.burym.crack_hash.model.generated.CrackHashWorkerResponse;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
public class ResultConsumerService {

    private final CrackHashService crackHashService;

    @Autowired
    public ResultConsumerService(final CrackHashService crackHashService) {
        this.crackHashService = crackHashService;
    }

    @RabbitListener(queues = "${rabbitmq.result.queue}", ackMode = "MANUAL")
    public void consume(final CrackHashWorkerResponse crackHashWorkerResponse,
                        final Channel channel, final Message message) throws IOException {
        try {
            log.info("consumer: received '{}'", new String(message.getBody()));

            crackHashService.updateTask(UUID.fromString(crackHashWorkerResponse.getRequestId()),
                    crackHashWorkerResponse.getAnswers().getWords());

            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            log.info("consumer: done with '{}'", new String(message.getBody()));
        } catch (final Exception e) {
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            log.error("consumer: error with '{}'", new String(message.getBody()), e);
        }
    }
}
