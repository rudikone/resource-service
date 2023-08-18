package ru.rudikov.resourceservice.application.service.auth;

import io.jsonwebtoken.Claims;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.rudikov.resourceservice.application.domain.model.auth.jwt.JwtAuthentication;
import ru.rudikov.resourceservice.application.domain.model.auth.jwt.Role;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JwtUtils {

  public static JwtAuthentication generate(Claims claims) {
    final JwtAuthentication jwtInfoToken = new JwtAuthentication();
    jwtInfoToken.setRoles(getRoles(claims));
    jwtInfoToken.setLogin(claims.getSubject());
    return jwtInfoToken;
  }

  private static Set<Role> getRoles(Claims claims) {
    final List<String> roles = claims.get("roles", List.class);
    return roles.stream().map(Role::valueOf).collect(Collectors.toSet());
  }
}
