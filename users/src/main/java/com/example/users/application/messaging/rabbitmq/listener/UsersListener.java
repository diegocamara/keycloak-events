package com.example.users.application.messaging.rabbitmq.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class UsersListener {

  @Autowired
  @Qualifier("usersConsumer")
  private Flux<String> usersConsumer;

  @PostConstruct
  public void postConstruct() {
    usersConsumer.subscribe(
        usersMessage -> {
          log.info(usersMessage);
        });
  }
}
