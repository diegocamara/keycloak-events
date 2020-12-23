package com.example.provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.ConnectionFactory;
import org.jboss.logging.Logger;
import org.keycloak.Config;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventListenerProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

public class RabbitMQEventListenerProviderFactory implements EventListenerProviderFactory {

  public static final String RABBITMQ_EVENT_LISTENER_ID = "rabbitmq-event-listener";
  private final ObjectMapper objectMapper = new ObjectMapper();
  private RabbitMQEventListenerConfiguration rabbitMQEventListenerConfiguration;
  private ConnectionFactory connectionFactory;
  private final Logger logger = Logger.getLogger(getClass());

  @Override
  public EventListenerProvider create(KeycloakSession session) {
    return new RabbitMQEventListenerProvider(
        connectionFactory, objectMapper, rabbitMQEventListenerConfiguration);
  }

  @Override
  public void init(Config.Scope config) {
    this.rabbitMQEventListenerConfiguration = new RabbitMQEventListenerConfiguration(config);
    this.connectionFactory = connectionFactory();
  }

  @Override
  public void postInit(KeycloakSessionFactory factory) {
    logger.info("RabbitMQEventListenerProviderFactory initialized with success.");
  }

  @Override
  public void close() {}

  @Override
  public String getId() {
    return RABBITMQ_EVENT_LISTENER_ID;
  }

  private ConnectionFactory connectionFactory() {
    final var connectionFactory = new ConnectionFactory();
    connectionFactory.setUsername(rabbitMQEventListenerConfiguration.getUsername());
    connectionFactory.setPassword(rabbitMQEventListenerConfiguration.getPassword());
    connectionFactory.setVirtualHost(rabbitMQEventListenerConfiguration.getVirtualHost());
    connectionFactory.setHost(rabbitMQEventListenerConfiguration.getHost());
    connectionFactory.setPort(rabbitMQEventListenerConfiguration.getPort());
    return connectionFactory;
  }
}
