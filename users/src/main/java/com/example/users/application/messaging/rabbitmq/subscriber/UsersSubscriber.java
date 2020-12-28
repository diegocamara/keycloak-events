package com.example.users.application.messaging.rabbitmq.subscriber;

import com.example.users.application.messaging.rabbitmq.model.UserEvent;
import com.example.users.domain.feature.CreateUser;
import com.example.users.domain.feature.DeleteUser;
import com.example.users.domain.feature.UpdateUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class UsersSubscriber {

  private final Flux<UserEvent> usersConsumer;
  private final CreateUser createUser;
  private final UpdateUser updateUser;
  private final DeleteUser deleteUser;

  public UsersSubscriber(
      @Qualifier("usersConsumer") Flux<UserEvent> usersConsumer,
      CreateUser createUser,
      UpdateUser updateUser,
      DeleteUser deleteUser) {
    this.usersConsumer = usersConsumer;
    this.createUser = createUser;
    this.updateUser = updateUser;
    this.deleteUser = deleteUser;
  }

  @PostConstruct
  public void postConstruct() {
    usersConsumer.subscribe(
        userEvent -> {
          log.info(userEvent.toString());
          switch (userEvent.getOperation()) {
            case CREATE:
              createUser
                  .handle(userEvent.toNewUser())
                  .subscribe(user -> log.info("User with id: " + user.getId() + " was created"));
              break;
            case UPDATE:
              updateUser
                  .handle(userEvent.toUpdateUserInfo())
                  .subscribe(user -> log.info("User with id: " + user.getId() + " was updated"));
              break;
            case DELETE:
              deleteUser
                  .handle(userEvent.getId())
                  .doOnSuccess(
                      unused -> log.info("User with id: " + userEvent.getId() + " was deleted"))
                  .subscribe();
              break;
            case ACTION:
              log.info("ACTION");
              break;
          }
        });
  }
}
