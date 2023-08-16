package ru.rudikov.resourceservice.application.port.primary;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.rudikov.resourceservice.application.domain.model.dto.UserDto;
import ru.rudikov.resourceservice.application.domain.model.dto.UserManagementDto;

public interface UserManagementPort {

    Mono<String> create(UserManagementDto user);

    Flux<UserDto> getAllUsers();

    Mono<UserDto> getUserById(String userId);

    Mono<UserManagementDto> updateUserById(String userId, UserManagementDto user);

    Mono<UserDto> deleteUserById(String userId);

    Flux<UserDto> searchUsers(String name);

    Flux<UserDto> streamAllUsers();
}
