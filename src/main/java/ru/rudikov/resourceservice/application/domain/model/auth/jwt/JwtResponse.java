package ru.rudikov.resourceservice.application.domain.model.auth.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtResponse {

  private final String type = "Bearer";
  private String accessToken;
  private String refreshToken;
}
