package ru.rudikov.resourceservice.adapter.primary.rest;

import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest authRequest) throws AuthException {
        final JwtResponse token = authPort.login(authRequest);
        return ResponseEntity.ok(token);
    }

    @PostMapping("token")
    public ResponseEntity<JwtResponse> getNewAccessToken(@RequestBody RefreshJwtRequest request) throws AuthException {
        final JwtResponse token = authPort.getAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

    @PostMapping("refresh")
    public ResponseEntity<JwtResponse> getNewRefreshToken(@RequestBody RefreshJwtRequest request) throws AuthException {
        final JwtResponse token = authPort.refresh(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

}
