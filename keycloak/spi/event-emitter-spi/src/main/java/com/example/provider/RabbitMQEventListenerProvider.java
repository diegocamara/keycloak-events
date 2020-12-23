package com.example.provider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.ConnectionFactory;
import org.jboss.logging.Logger;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.admin.AdminEvent;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitMQEventListenerProvider implements EventListenerProvider {

  private final ConnectionFactory connectionFactory;
  private final ObjectMapper objectMapper;
  private final RabbitMQEventListenerConfiguration rabbitMQEventListenerConfiguration;
  private final Logger logger = Logger.getLogger(getClass());

  public RabbitMQEventListenerProvider(
      ConnectionFactory connectionFactory,
      ObjectMapper objectMapper,
      RabbitMQEventListenerConfiguration rabbitMQEventListenerConfiguration) {
    this.connectionFactory = connectionFactory;
    this.objectMapper = objectMapper;
    this.rabbitMQEventListenerConfiguration = rabbitMQEventListenerConfiguration;
  }

  @Override
  public void onEvent(Event event) {
    if (rabbitMQEventListenerConfiguration.isPublishAllEvents()) {
      publishEventMessage(writeValueAsString(event));
      logger.info("Event of type " + event.getType() + " published with success.");
    }
  }

  @Override
  public void onEvent(AdminEvent event, boolean includeRepresentation) {
    if (rabbitMQEventListenerConfiguration.isPublishAdminEvents()) {
      publishEventMessage(writeValueAsString(event));
      logger.info("Admin event published with success.");
    }
  }

  @Override
  public void close() {}

  private void publishEventMessage(String event) {
    try {
      try (final var connection = connectionFactory.newConnection()) {

        try (final var channel = connection.createChannel()) {

          final String targetQueue = rabbitMQEventListenerConfiguration.getTargetQueue();

          channel.queueDeclare(targetQueue, false, false, false, null);

          channel.basicPublish(
              "",
              targetQueue,
              new AMQP.BasicProperties()
                  .builder()
                  .contentType("application/json")
                  .contentEncoding("UTF-8")
                  .deliveryMode(2)
                  .build(),
              event.getBytes());
        }
      }

    } catch (IOException | TimeoutException e) {
      throw new RuntimeException(e);
    }
  }

  private String writeValueAsString(Object value) {
    try {
      return objectMapper.writeValueAsString(value);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
