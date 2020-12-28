package com.example.users.infrastructure.configuration;

import com.example.users.application.messaging.rabbitmq.model.UserEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

@Slf4j
@Configuration
public class RabbitMQConfiguration {

  @Autowired private ObjectMapper objectMapper;

  @Value("${queue.users-events}")
  private String usersEventsQueue;

  @Bean("usersFlux")
  public Flux<UserEvent> usersFlux(ConnectionFactory connectionFactory) {

    final var messageListenerContainer = new SimpleMessageListenerContainer(connectionFactory);
    messageListenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);
    messageListenerContainer.addQueueNames(usersEventsQueue);

    return Flux.create(
        emitter -> {
          messageListenerContainer.setMessageListener(
              message -> {
                final var userEvent = readValue(message.getBody(), UserEvent.class);
                emitter.next(userEvent);
              });

          emitter.onRequest(value -> messageListenerContainer.start());

          emitter.onDispose(messageListenerContainer::stop);
        });
  }

  @SneakyThrows
  private <T> T readValue(byte[] value, Class<T> clazz) {
    return objectMapper.readValue(value, clazz);
  }
}
