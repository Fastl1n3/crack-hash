package ru.nsu.burym.crack_hash.worker.service.rabbit;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nsu.burym.crack_hash.model.generated.CrackHashManagerRequest;
import ru.nsu.burym.crack_hash.worker.service.TaskService;

import java.io.IOException;

@Slf4j
@Service
public class TaskConsumerService {

    private final TaskService taskService;

    @Autowired
    public TaskConsumerService(final TaskService taskService) {
        this.taskService = taskService;
    }

    @RabbitListener(queues = "${rabbitmq.tasks.queue}", ackMode = "MANUAL")
    public void consume(final CrackHashManagerRequest crackHashManagerRequest,
                        final Channel channel, final Message message) throws IOException {
        try {
            log.info("consumer: received '{}'", new String(message.getBody()));
            taskService.executeTask(crackHashManagerRequest);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            log.info("consumer: done with '{}'", new String(message.getBody()));
        } catch (final Exception e) {
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
        }
    }
}