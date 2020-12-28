package com.example.provider;

import org.keycloak.Config;

public class RabbitMQEventListenerConfiguration {

  public static final String USERNAME = "username";
  public static final String PASSWORD = "password";
  public static final String VIRTUAL_HOST = "virtual_host";
  public static final String HOST = "host";
  public static final String PORT = "port";
  public static final String TARGET_QUEUE = "target_queue";
  public static final String PUBLISH_ADMIN_USERS_EVENTS = "publish_admin_users_events";

  public static final String REALM_ID = "realm_id";

  private final String username;
  private final String password;
  private final String virtualHost;
  private final String host;
  private final Integer port;
  private final String targetQueue;
  private final boolean publishAdminUsersEvents;
  private final String realmId;

  public RabbitMQEventListenerConfiguration(Config.Scope config) {
    this.username = config.get(USERNAME);
    this.password = config.get(PASSWORD);
    this.virtualHost = config.get(VIRTUAL_HOST);
    this.host = config.get(HOST);
    this.port = config.getInt(PORT);
    this.targetQueue = config.get(TARGET_QUEUE);
    this.publishAdminUsersEvents = config.getBoolean(PUBLISH_ADMIN_USERS_EVENTS);
    this.realmId = config.get(REALM_ID);
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public String getVirtualHost() {
    return virtualHost;
  }

  public String getHost() {
    return host;
  }

  public Integer getPort() {
    return port;
  }

  public String getTargetQueue() {
    return targetQueue;
  }

  public boolean isPublishAdminUsersEvents() {
    return publishAdminUsersEvents;
  }

  public String getRealmId() {
    return realmId;
  }
}
