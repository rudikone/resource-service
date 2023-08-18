package ru.rudikov.resourceservice.application.port.primary;

import lombok.NonNull;
import reactor.core.publisher.Mono;
import ru.rudikov.resourceservice.application.domain.model.auth.jwt.JwtRequest;
import ru.rudikov.resourceservice.application.domain.model.auth.jwt.JwtResponse;

public interface AuthPort {

  Mono<JwtResponse> login(@NonNull JwtRequest authRequest);

  Mono<JwtResponse> getAccessToken(@NonNull String refreshToken);

  Mono<JwtResponse> refresh(@NonNull String refreshToken);
}
