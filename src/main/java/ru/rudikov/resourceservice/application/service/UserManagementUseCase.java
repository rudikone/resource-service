package ru.rudikov.resourceservice.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import ru.rudikov.resourceservice.application.domain.model.dto.UserDto;
import ru.rudikov.resourceservice.application.domain.model.dto.UserManagementDto;
import ru.rudikov.resourceservice.application.port.primary.UserManagementPort;
import ru.rudikov.resourceservice.application.port.secondary.UserPort;

import java.time.Duration;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class UserManagementUseCase implements UserManagementPort {

    private final UserPort userPort;

    @Override
    public Mono<String> create(UserManagementDto user) {
        return userPort.create(user);
    }

    @Override
    public Flux<UserDto> getAllUsers() {
        return userPort.getAllUsers();
    }

    @Override
    public Mono<UserDto> getUserById(String userId) {
        return userPort.getUserById(userId);
    }

    @Override
    public Mono<UserManagementDto> updateUserById(String userId, UserManagementDto user) {
        return userPort.updateUserById(userId, user);
    }

    @Override
    public Mono<UserDto> deleteUserById(String userId) {
        return userPort.deleteUserById(userId);
    }

    @Override
    public Flux<UserDto> searchUsers(String name) {
        return userPort.searchUsers(name);
    }

    @Override
    public Flux<UserDto> streamAllUsers() {
        return userPort.getAllUsers().flatMap(user ->
                Flux.zip(Flux.interval(Duration.ofSeconds(2)), Flux.fromStream(Stream.generate(() -> user)))
                        .map(Tuple2::getT2)
        );
    }
}
