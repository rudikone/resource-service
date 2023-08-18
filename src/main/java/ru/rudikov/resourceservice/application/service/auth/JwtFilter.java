package ru.rudikov.resourceservice.application.service.auth;

import io.jsonwebtoken.Claims;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import ru.rudikov.resourceservice.application.domain.model.auth.jwt.JwtAuthentication;

@Component
@RequiredArgsConstructor
public class JwtFilter implements WebFilter {

  private static final String AUTHORIZATION = "Authorization";

  private final JwtService jwtService;

  @Override
  public Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
    String token = getTokenFromRequest(exchange.getRequest());

    if (token != null && jwtService.validateAccessToken(token)) {
      final Claims claims = jwtService.getAccessClaims(token);
      final JwtAuthentication jwtInfoToken = JwtUtils.generate(claims);
      jwtInfoToken.setAuthenticated(true);

      return chain
          .filter(exchange)
          .contextWrite(ReactiveSecurityContextHolder.withAuthentication(jwtInfoToken));
    }

    return chain.filter(exchange);
  }

  private String getTokenFromRequest(ServerHttpRequest request) {
    final String bearer = request.getHeaders().getFirst(AUTHORIZATION);
    if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
      return bearer.substring(7);
    }
    return null;
  }
}
