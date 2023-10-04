package ru.rudikov.resourceservice.adapter.secondary.db.token;

import java.time.Duration;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.rudikov.resourceservice.application.port.secondary.RefreshTokenPort;

@Service
@RequiredArgsConstructor
public class RefreshTokenAdapter implements RefreshTokenPort {

  private final ReactiveRedisOperations<String, String> redisOperations;

  @Override
  public Mono<String> getByLogin(@NonNull String login) {
    return redisOperations.opsForValue().get(login);
  }

  @Override
  public Mono<Boolean> put(@NonNull String login, @NonNull String token) {
    return redisOperations.opsForValue().set(login, token, Duration.ofDays(30));
  }

  @Override
  public Mono<Long> getUsersCount() {
    return redisOperations.keys("*").count();
  }

  @Override
  public Mono<Boolean> deleteByLogin(@NonNull String login) {
    return redisOperations.opsForValue().delete(login);
  }
}
