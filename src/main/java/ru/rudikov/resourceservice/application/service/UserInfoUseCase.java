package ru.rudikov.resourceservice.application.service;

import static java.time.Duration.ofSeconds;
import static reactor.core.publisher.Flux.*;

import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import ru.rudikov.resourceservice.application.domain.model.dto.UserDto;
import ru.rudikov.resourceservice.application.port.primary.UserInfoPort;
import ru.rudikov.resourceservice.application.port.secondary.UserPort;
import ru.rudikov.resourceservice.application.service.transform.UserMapper;

@Service
@RequiredArgsConstructor
public class UserInfoUseCase implements UserInfoPort {

  private final UserPort userPort;
  private final UserMapper mapper;

  @Override
  public Flux<UserDto> getAllUsers() {
    return userPort.getAllUsers().map(mapper::toUserDto);
  }

  @Override
  public Mono<UserDto> getUserById(String userId) {
    return userPort.getUserById(userId).map(mapper::toUserDto);
  }

  @Override
  public Flux<UserDto> searchUsers(String name) {
    return userPort.searchUsers(name).map(mapper::toUserDto);
  }

  @Override
  public Flux<UserDto> streamAllUsers() {
    return userPort
        .getAllUsers()
        .map(mapper::toUserDto)
        .flatMap(
            user ->
                zip(interval(ofSeconds(2)), fromStream(Stream.generate(() -> user)))
                    .map(Tuple2::getT2));
  }
}
