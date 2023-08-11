package ru.rudikov.resourceservice.application.port.primary;

import lombok.NonNull;
import ru.rudikov.resourceservice.application.domain.exception.AuthException;
import ru.rudikov.resourceservice.application.domain.model.auth.jwt.JwtRequest;
import ru.rudikov.resourceservice.application.domain.model.auth.jwt.JwtResponse;

public interface AuthPort {

    JwtResponse login(@NonNull JwtRequest authRequest) throws AuthException;

    JwtResponse getAccessToken(@NonNull String refreshToken) throws AuthException;

    JwtResponse refresh(@NonNull String refreshToken) throws AuthException;
}
