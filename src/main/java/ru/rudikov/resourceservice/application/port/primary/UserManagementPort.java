package ru.rudikov.resourceservice.application.port.primary;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.rudikov.resourceservice.application.domain.model.dto.UserManagementDto;

public interface UserManagementPort {
  Mono<String> create(UserManagementDto user);

  Flux<UserManagementDto> getAllUsers();

  Mono<UserManagementDto> getUserById(String userId);

  Mono<UserManagementDto> updateUserById(String userId, UserManagementDto user);

  Mono<UserManagementDto> deleteUserById(String userId);

  Flux<UserManagementDto> searchUsers(String name);

  Flux<UserManagementDto> streamAllUsers();
}
