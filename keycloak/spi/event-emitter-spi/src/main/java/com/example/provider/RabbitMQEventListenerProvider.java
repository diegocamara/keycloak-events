package com.example.provider;

import com.example.provider.model.UserEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.ConnectionFactory;
import org.jboss.logging.Logger;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.events.admin.ResourceType;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

public class RabbitMQEventListenerProvider implements EventListenerProvider {

  public static final ResourceType USER_RESOURCE = ResourceType.USER;
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
  public void onEvent(Event event) {}

  @Override
  public void onEvent(AdminEvent adminEvent, boolean includeRepresentation) {
    if (isUserResource(adminEvent.getResourceType())
        && isValidRealmId(adminEvent.getRealmId())
        && rabbitMQEventListenerConfiguration.isPublishAdminUsersEvents()) {
      final var userEvent = userEvent(adminEvent);
      publishEventMessage(writeValueAsBytes(userEvent));
      logger.info("Admin event published with success.");
    }
  }

  @Override
  public void close() {}

  private UserEvent userEvent(AdminEvent adminEvent) {
    final var userEvent = new UserEvent();
    userEvent.setId(UUID.fromString(userId(adminEvent)));
    userEvent.setOperation(operation(adminEvent));
    userEvent.setRepresentation(representation(adminEvent));
    return userEvent;
  }

  private String userId(AdminEvent adminEvent) {
    final var resourcePath = adminEvent.getResourcePath();
    return resourcePath.substring(resourcePath.lastIndexOf('/') + 1);
  }

  private UserEvent.OperationType operation(AdminEvent adminEvent) {
    UserEvent.OperationType operation = null;
    switch (adminEvent.getOperationType()) {
      case CREATE:
        operation = UserEvent.OperationType.CREATE;
        break;
      case UPDATE:
        operation = UserEvent.OperationType.UPDATE;
        break;
      case DELETE:
        operation = UserEvent.OperationType.DELETE;
        break;
      case ACTION:
        operation = UserEvent.OperationType.ACTION;
        break;
    }
    return operation;
  }

  private Map<String, Object> representation(AdminEvent adminEvent) {
    final var representation = adminEvent.getRepresentation();
    if (representation != null) {
      final var typedReference = new TypeReference<HashMap<String, Object>>() {};
      try {
        return objectMapper.readValue(representation, typedReference);
      } catch (JsonProcessingException e) {
        throw new RuntimeException(e);
      }
    }
    return null;
  }

  private boolean isValidRealmId(String realmId) {
    return rabbitMQEventListenerConfiguration.getRealmId().trim().equalsIgnoreCase(realmId);
  }

  private boolean isUserResource(ResourceType resourceType) {
    return USER_RESOURCE.equals(resourceType);
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
