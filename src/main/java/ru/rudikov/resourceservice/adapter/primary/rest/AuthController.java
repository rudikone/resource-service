package ru.rudikov.resourceservice.adapter.primary.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.rudikov.resourceservice.application.domain.exception.AuthException;
import ru.rudikov.resourceservice.application.domain.model.auth.jwt.JwtRequest;
import ru.rudikov.resourceservice.application.domain.model.auth.jwt.JwtResponse;
import ru.rudikov.resourceservice.application.domain.model.auth.jwt.RefreshJwtRequest;
import ru.rudikov.resourceservice.application.port.primary.AuthPort;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthPort authPort;

    @PostMapping("login")
    public Mono<JwtResponse> login(@RequestBody JwtRequest authRequest) throws AuthException {
        final JwtResponse token = authPort.login(authRequest);
        return Mono.just(token);
    }

    @PostMapping("token")
    public Mono<JwtResponse> getNewAccessToken(@RequestBody RefreshJwtRequest request) throws AuthException {
        final JwtResponse token = authPort.getAccessToken(request.getRefreshToken());
        return Mono.just(token);
    }

    @PostMapping("refresh")
    public Mono<JwtResponse> getNewRefreshToken(@RequestBody RefreshJwtRequest request) throws AuthException {
        final JwtResponse token = authPort.refresh(request.getRefreshToken());
        return Mono.just(token);
    }

}