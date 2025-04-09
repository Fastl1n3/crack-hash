package ru.nsu.burym.crack_hash.manager.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MarshallingMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.tasks.exchange}")
    private String taskExchange;

    @Value("${rabbitmq.tasks.queue}")
    private String taskQueue;

    @Value("${rabbitmq.tasks.key}")
    private String taskKey;

    @Value("${rabbitmq.result.exchange}")
    private String resultExchange;

    @Value("${rabbitmq.result.queue}")
    private String resultQueue;

    @Value("${rabbitmq.result.key}")
    private String resultKey;

    @Bean
    public Queue taskQueue() {
        return QueueBuilder.durable(taskQueue).build();
    }

    @Bean
    public DirectExchange taskExchange() {
        return new DirectExchange(taskExchange, true, false);
    }

    @Bean
    public Binding taskBinding() {
        return BindingBuilder
                .bind(taskQueue())
                .to(taskExchange())
                .with(taskKey);
    }

    @Bean
    public Queue resultQueue() {
        return QueueBuilder.durable(resultQueue).build();
    }

    @Bean
    public DirectExchange resultExchange() {
        return new DirectExchange(resultExchange, true, false);
    }

    @Bean
    public Binding resultBinding() {
        return BindingBuilder
                .bind(resultQueue())
                .to(resultExchange())
                .with(resultKey);
    }

    @Bean
    public Jaxb2Marshaller jaxb2Marshaller() {
        final Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setPackagesToScan("ru.nsu.burym.crack_hash.model.generated"); // пакет с DTO
        return marshaller;
    }

    @Bean
    public MarshallingMessageConverter marshallingMessageConverter(final Jaxb2Marshaller marshaller) {
        return new MarshallingMessageConverter(marshaller);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory,
                                         final MarshallingMessageConverter messageConverter) {
        final RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }
}
