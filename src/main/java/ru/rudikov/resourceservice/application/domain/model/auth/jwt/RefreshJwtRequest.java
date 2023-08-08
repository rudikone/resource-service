package ru.rudikov.resourceservice.application.domain.model.auth.jwt;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshJwtRequest {

    public String refreshToken;
}
