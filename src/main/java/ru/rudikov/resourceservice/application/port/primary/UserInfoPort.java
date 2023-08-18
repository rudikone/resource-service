package ru.rudikov.resourceservice.application.port.primary;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.rudikov.resourceservice.application.domain.model.dto.UserDto;

public interface UserInfoPort {
  Flux<UserDto> getAllUsers();

  Mono<UserDto> getUserById(String userId);

  Flux<UserDto> searchUsers(String name);

  Flux<UserDto> streamAllUsers();
}
