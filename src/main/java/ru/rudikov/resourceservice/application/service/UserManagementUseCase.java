package ru.rudikov.resourceservice.application.service;

import static java.time.Duration.ofSeconds;
import static reactor.core.publisher.Flux.*;

import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import ru.rudikov.resourceservice.application.domain.model.dto.UserManagementDto;
import ru.rudikov.resourceservice.application.port.primary.UserManagementPort;
import ru.rudikov.resourceservice.application.port.secondary.UserPort;

@Service
@RequiredArgsConstructor
public class UserManagementUseCase implements UserManagementPort {

  private final UserPort userPort;

  @Override
  public Mono<String> create(UserManagementDto user) {
    return userPort.create(user);
  }

  @Override
  public Flux<UserManagementDto> getAllUsers() {
    return userPort.getAllUsers();
  }

  @Override
  public Mono<UserManagementDto> getUserById(String userId) {
    return userPort.getUserById(userId);
  }

  @Override
  public Mono<UserManagementDto> updateUserById(String userId, UserManagementDto user) {
    return userPort.updateUserById(userId, user);
  }

  @Override
  public Mono<UserManagementDto> deleteUserById(String userId) {
    return userPort.deleteUserById(userId);
  }

  @Override
  public Flux<UserManagementDto> searchUsers(String name) {
    return userPort.searchUsers(name);
  }

  @Override
  public Flux<UserManagementDto> streamAllUsers() {
    return userPort
        .getAllUsers()
        .flatMap(
            user ->
                zip(interval(ofSeconds(2)), fromStream(Stream.generate(() -> user)))
                    .map(Tuple2::getT2));
  }
}
