package ru.rudikov.resourceservice.application.port.secondary;

import lombok.NonNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.rudikov.resourceservice.application.domain.model.dto.UserManagementDto;

public interface UserPort {

  Mono<UserManagementDto> getByLogin(@NonNull String login);

  Mono<String> create(UserManagementDto user);

  Flux<UserManagementDto> getAllUsers();

  Mono<UserManagementDto> getUserById(String userId);

  Mono<UserManagementDto> updateUserById(String userId, UserManagementDto user);

  Mono<UserManagementDto> deleteUserById(String userId);

  Flux<UserManagementDto> searchUsers(String name);
}
