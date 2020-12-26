package com.example.users.infrastructure.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

@Slf4j
@Configuration
public class RabbitMQConfiguration {

  @Bean("usersConsumer")
  public Flux<String> usersConsumer(ConnectionFactory connectionFactory) {

    final var messageListenerContainer = new SimpleMessageListenerContainer(connectionFactory);
    messageListenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);
    messageListenerContainer.addQueueNames("keycloak-events");

    return Flux.create(
        emitter -> {
          messageListenerContainer.setMessageListener(
              message -> {
                log.info(message.getMessageProperties().toString());
                final var payload = new String(message.getBody());
                emitter.next(payload);
              });

          emitter.onRequest(value -> messageListenerContainer.start());

          emitter.onDispose(messageListenerContainer::stop);
        });
  }
}
