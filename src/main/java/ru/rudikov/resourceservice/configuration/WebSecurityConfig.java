package ru.rudikov.resourceservice.configuration;

import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.security.config.web.server.SecurityWebFiltersOrder.AUTHENTICATION;
import static ru.rudikov.resourceservice.application.domain.model.auth.jwt.Role.ADMIN;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity.CsrfSpec;
import org.springframework.security.config.web.server.ServerHttpSecurity.FormLoginSpec;
import org.springframework.security.config.web.server.ServerHttpSecurity.HttpBasicSpec;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;
import ru.rudikov.resourceservice.application.service.auth.JwtFilter;

@RequiredArgsConstructor
@Slf4j
@Configuration
@EnableWebFluxSecurity
public class WebSecurityConfig {

  private final JwtFilter jwtFilter;

  @Bean
  public SecurityWebFilterChain filterChain(ServerHttpSecurity http) {
    http.httpBasic(HttpBasicSpec::disable)
        .formLogin(FormLoginSpec::disable)
        .csrf(CsrfSpec::disable)
        .exceptionHandling(
            exceptionHandlingSpec ->
                exceptionHandlingSpec.authenticationEntryPoint(
                    (exchange, ex) -> {
                      log.error("Auth exception", ex);
                      return Mono.fromRunnable(
                          () -> {
                            ServerHttpResponse response = exchange.getResponse();
                            response.setStatusCode(UNAUTHORIZED);
                          });
                    }))
        .authorizeExchange(
            authorizeExchangeSpec ->
                authorizeExchangeSpec
                    .pathMatchers(
                        "/api/auth/login", "/api/auth/token", "/swagger-doc/**", "/actuator/**")
                    .permitAll()
                    .pathMatchers(POST, "/resource/**")
                    .hasAuthority(ADMIN.getAuthority())
                    .pathMatchers("/management/**")
                    .hasAuthority(ADMIN.getAuthority())
                    .anyExchange()
                    .authenticated())
        .addFilterAt(jwtFilter, AUTHENTICATION);

    return http.build();
  }
}
