package ru.rudikov.resourceservice.application.service;

import io.jsonwebtoken.Claims;
import jakarta.security.auth.message.AuthException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rudikov.resourceservice.application.domain.model.auth.jwt.JwtRequest;
import ru.rudikov.resourceservice.application.domain.model.auth.jwt.JwtResponse;
import ru.rudikov.resourceservice.application.domain.model.dto.User;
import ru.rudikov.resourceservice.application.port.primary.AuthPort;
import ru.rudikov.resourceservice.application.service.auth.JwtService;
import ru.rudikov.resourceservice.application.service.auth.UserService;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthUseCase implements AuthPort {

    private final UserService userService;
    private final JwtService jwtService;
    private final Map<String, String> refreshStorage = new HashMap<>(); //использовать хранилище


    @Override
    public JwtResponse login(@NonNull JwtRequest authRequest) throws AuthException {
        final User user = userService.getByLogin(authRequest.getLogin())
                .orElseThrow(() -> new AuthException("Пользователь не найден"));

        if (user.getPassword().equals(authRequest.getPassword())) {
            final String accessToken = jwtService.generateAccessToken(user);
            final String refreshToken = jwtService.generateRefreshToken(user);
            refreshStorage.put(user.getLogin(), refreshToken);
            return new JwtResponse(accessToken, refreshToken);
        } else {
            throw new AuthException("Неправильный пароль");
        }
    }

    @Override
    public JwtResponse getAccessToken(@NonNull String refreshToken) throws AuthException {
        if (jwtService.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtService.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String refreshTokenFromDb = refreshStorage.get(login);

            if (refreshTokenFromDb != null && refreshTokenFromDb.equals(refreshToken)) {
                final User user = userService.getByLogin(login)
                        .orElseThrow(() -> new AuthException("Пользователь не найден"));
                final String accessToken = jwtService.generateAccessToken(user);
                return new JwtResponse(accessToken, null);
            }
        }
        return new JwtResponse(null, null);
    }

    @Override
    public JwtResponse refresh(@NonNull String refreshToken) throws AuthException {
        if (jwtService.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtService.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String refreshTokenFromDb = refreshStorage.get(login);

            if (refreshTokenFromDb != null && refreshTokenFromDb.equals(refreshToken)) {
                final User user = userService.getByLogin(login)
                        .orElseThrow(() -> new AuthException("Пользователь не найден"));
                final String accessToken = jwtService.generateAccessToken(user);
                final String newRefreshToken = jwtService.generateRefreshToken(user);
                refreshStorage.put(user.getLogin(), newRefreshToken);
                return new JwtResponse(accessToken, newRefreshToken);
            }
        }
        throw new AuthException("Невалидный JWT токен");
    }
}
