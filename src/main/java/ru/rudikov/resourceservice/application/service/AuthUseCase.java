package ru.rudikov.resourceservice.application.service;

import static ru.rudikov.resourceservice.application.service.MetricHelper.FAILED_RESULT;

import io.jsonwebtoken.Claims;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.rudikov.resourceservice.application.domain.exception.AuthException;
import ru.rudikov.resourceservice.application.domain.model.auth.jwt.JwtAuthentication;
import ru.rudikov.resourceservice.application.domain.model.auth.jwt.JwtRequest;
import ru.rudikov.resourceservice.application.domain.model.auth.jwt.JwtResponse;
import ru.rudikov.resourceservice.application.port.primary.AuthPort;
import ru.rudikov.resourceservice.application.port.secondary.RefreshTokenPort;
import ru.rudikov.resourceservice.application.port.secondary.UserPort;
import ru.rudikov.resourceservice.application.service.auth.JwtService;

@Service
@RequiredArgsConstructor
public class AuthUseCase implements AuthPort {

  private final UserPort userPort;
  private final RefreshTokenPort refreshTokenPort;
  private final JwtService jwtService;
  private final MetricHelper metricHelper;

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

                return refreshTokenPort
                    .getByLogin(userDto.getLogin())
                    .flatMap(refreshToken -> Mono.just(new JwtResponse(accessToken, refreshToken)))
                    .switchIfEmpty(
                        Mono.defer(
                            () -> {
                              final String refreshToken = jwtService.generateRefreshToken(userDto);
                              return refreshTokenPort
                                  .put(userDto.getLogin(), refreshToken)
                                  .then(Mono.just(new JwtResponse(accessToken, refreshToken)));
                            }))
                    .flatMap(
                        jwtResponse ->
                            refreshTokenPort
                                .getUsersCount()
                                .doOnSuccess(metricHelper::updateUserGauge)
                                .then(Mono.just(jwtResponse)));
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
      return refreshTokenPort
          .getByLogin(login)
          .flatMap(
              refreshTokenFromCache -> {
                if (refreshTokenFromCache.equals(refreshToken)) {
                  return userPort
                      .getByLogin(login)
                      .switchIfEmpty(Mono.error(new AuthException("Пользователь не найден")))
                      .flatMap(
                          userDto -> {
                            final String accessToken = jwtService.generateAccessToken(userDto);
                            return Mono.just(new JwtResponse(accessToken, null));
                          });
                } else {
                  return Mono.just(new JwtResponse(null, null));
                }
              })
          .switchIfEmpty(Mono.just(new JwtResponse(null, null)));
    }
    return Mono.just(new JwtResponse(null, null));
  }

  @Override
  public Mono<JwtResponse> refresh(@NonNull String refreshToken) {
    if (jwtService.validateRefreshToken(refreshToken)) {
      final Claims claims = jwtService.getRefreshClaims(refreshToken);
      final String login = claims.getSubject();
      return refreshTokenPort
          .getByLogin(login)
          .flatMap(
              refreshTokenFromCache -> {
                if (refreshTokenFromCache.equals(refreshToken)) {
                  return userPort
                      .getByLogin(login)
                      .switchIfEmpty(Mono.error(new AuthException("Пользователь не найден")))
                      .flatMap(
                          userDto -> {
                            final String accessToken = jwtService.generateAccessToken(userDto);
                            final String newRefreshToken = jwtService.generateRefreshToken(userDto);
                            return refreshTokenPort
                                .put(login, newRefreshToken)
                                .then(Mono.just(new JwtResponse(accessToken, newRefreshToken)));
                          });
                }
                {
                  return Mono.error(new AuthException("Невалидный JWT токен"));
                }
              })
          .switchIfEmpty(Mono.error(new AuthException("Невалидный JWT токен")));
    }
    return Mono.error(new AuthException("Невалидный JWT токен"));
  }

  @Override
  public Mono<Boolean> logout() {
    return ReactiveSecurityContextHolder.getContext()
        .map(SecurityContext::getAuthentication)
        .map(JwtAuthentication.class::cast)
        .flatMap(
            jwtAuthentication -> {
              var login = jwtAuthentication.getLogin();
//              jwtAuthentication.setAuthenticated(false);
//              ReactiveSecurityContextHolder.withAuthentication(jwtAuthentication);
              return refreshTokenPort
                  .deleteByLogin(login)
                  .flatMap(
                      result ->
                          refreshTokenPort
                              .getUsersCount()
                              .doOnSuccess(metricHelper::updateUserGauge)
                              .then(Mono.just(result)));
            });
  }
}
