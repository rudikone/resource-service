package ru.rudikov.resourceservice.application.port.secondary;

import lombok.NonNull;
import reactor.core.publisher.Mono;

public interface RefreshTokenPort {

  Mono<String> getByLogin(@NonNull String login);

  Mono<Boolean> put(@NonNull String login, @NonNull String token);

  Mono<Long> getUsersCount();

  public Mono<Boolean> deleteByLogin(@NonNull String login);
}
