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
    if (isValidRealmId(event.getRealmId())
        && rabbitMQEventListenerConfiguration.isPublishAllEvents()) {
      publishEventMessage(writeValueAsBytes(event));
      logger.info("Event of type " + event.getType() + " published with success.");
    }
  }

  @Override
  public void onEvent(AdminEvent event, boolean includeRepresentation) {
    if (isValidRealmId(event.getRealmId())
        && rabbitMQEventListenerConfiguration.isPublishAdminEvents()) {
      publishEventMessage(writeValueAsBytes(event));
      logger.info("Admin event published with success.");
    }
  }

  @Override
  public void close() {}

  private boolean isValidRealmId(String realmId) {
    return rabbitMQEventListenerConfiguration.getRealmId().trim().equalsIgnoreCase(realmId);
  }

  private void publishEventMessage(byte[] event) {
    try {
      try (final var connection = connectionFactory.newConnection()) {

        try (final var channel = connection.createChannel()) {

          final String targetQueue = rabbitMQEventListenerConfiguration.getTargetQueue();

          channel.queueDeclare(targetQueue, true, false, false, null);

          channel.basicPublish(
              "",
              targetQueue,
              new AMQP.BasicProperties()
                  .builder()
                  .contentType("application/json")
                  .contentEncoding("UTF-8")
                  .deliveryMode(2)
                  .build(),
              event);
        }
      }

    } catch (IOException | TimeoutException e) {
      throw new RuntimeException(e);
    }
  }

  private byte[] writeValueAsBytes(Object value) {
    try {
      return objectMapper.writeValueAsBytes(value);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
