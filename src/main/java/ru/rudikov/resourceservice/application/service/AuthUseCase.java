package ru.rudikov.resourceservice.application.service;

import static ru.rudikov.resourceservice.application.service.MetricHelper.FAILED_RESULT;
import static ru.rudikov.resourceservice.application.service.MetricHelper.SUCCESS_RESULT;

import io.jsonwebtoken.Claims;
import java.util.HashMap;
import java.util.Map;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.rudikov.resourceservice.application.domain.exception.AuthException;
import ru.rudikov.resourceservice.application.domain.model.auth.jwt.JwtRequest;
import ru.rudikov.resourceservice.application.domain.model.auth.jwt.JwtResponse;
import ru.rudikov.resourceservice.application.port.primary.AuthPort;
import ru.rudikov.resourceservice.application.port.secondary.UserPort;
import ru.rudikov.resourceservice.application.service.auth.JwtService;

@Service
@RequiredArgsConstructor
public class AuthUseCase implements AuthPort {

  private final UserPort userPort;
  private final JwtService jwtService;
  private final MetricHelper metricHelper;
  private final Map<String, String> refreshStorage = new HashMap<>(); // использовать хранилище

  @Override
  public Mono<JwtResponse> login(@NonNull JwtRequest authRequest) {
    return userPort
        .getByLogin(authRequest.getLogin())
        .switchIfEmpty(
            Mono.defer(
                () -> {
                  metricHelper.loginCounter(FAILED_RESULT).increment();

                  return Mono.error(new AuthException("Пользователь не найден"));
                }))
        .flatMap(
            userDto -> {
              if (userDto.getPassword().equals(authRequest.getPassword())) {
                final String accessToken = jwtService.generateAccessToken(userDto);
                final String refreshToken = jwtService.generateRefreshToken(userDto);
                refreshStorage.put(userDto.getLogin(), refreshToken);

                metricHelper.loginCounter(SUCCESS_RESULT).increment();
                metricHelper.updateUserGauge(refreshStorage.size());

                return Mono.just(new JwtResponse(accessToken, refreshToken));
              } else {
                metricHelper.loginCounter(FAILED_RESULT).increment();

                return Mono.error(new AuthException("Неправильный пароль"));
              }
            });
  }

  @Override
  public Mono<JwtResponse> getAccessToken(@NonNull String refreshToken) {
    if (jwtService.validateRefreshToken(refreshToken)) {
      final Claims claims = jwtService.getRefreshClaims(refreshToken);
      final String login = claims.getSubject();
      final String refreshTokenFromDb = refreshStorage.get(login);

      if (refreshTokenFromDb != null && refreshTokenFromDb.equals(refreshToken)) {
        return userPort
            .getByLogin(login)
            .switchIfEmpty(Mono.error(new AuthException("Пользователь не найден")))
            .flatMap(
                userDto -> {
                  final String accessToken = jwtService.generateAccessToken(userDto);
                  return Mono.just(new JwtResponse(accessToken, null));
                });
      }
    }
    return Mono.just(new JwtResponse(null, null));
  }

  @Override
  public Mono<JwtResponse> refresh(@NonNull String refreshToken) {
    if (jwtService.validateRefreshToken(refreshToken)) {
      final Claims claims = jwtService.getRefreshClaims(refreshToken);
      final String login = claims.getSubject();
      final String refreshTokenFromDb = refreshStorage.get(login);

      if (refreshTokenFromDb != null && refreshTokenFromDb.equals(refreshToken)) {
        return userPort
            .getByLogin(login)
            .switchIfEmpty(Mono.error(new AuthException("Пользователь не найден")))
            .flatMap(
                userDto -> {
                  final String accessToken = jwtService.generateAccessToken(userDto);
                  final String newRefreshToken = jwtService.generateRefreshToken(userDto);
                  refreshStorage.put(userDto.getLogin(), newRefreshToken);
                  return Mono.just(new JwtResponse(accessToken, newRefreshToken));
                });
      }
    }
    return Mono.error(new AuthException("Невалидный JWT токен"));
  }
}
