package ru.rudikov.resourceservice.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import ru.rudikov.resourceservice.application.service.auth.JwtFilter;

import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.web.server.SecurityWebFiltersOrder.AUTHENTICATION;
import static ru.rudikov.resourceservice.application.domain.model.dto.Role.ADMIN;

@RequiredArgsConstructor
@Configuration
@EnableWebFluxSecurity
public class WebSecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http) {
        http.httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(authorizeExchangeCustomizer ->
                        authorizeExchangeCustomizer
                                .pathMatchers("/api/auth/login", "/api/auth/token")
                                .permitAll()
                                .pathMatchers(POST, "/resource/**")
                                .hasAuthority(ADMIN.getAuthority())
                                .anyExchange()
                                .authenticated()
                )
                .addFilterAt(jwtFilter, AUTHENTICATION);

        return http.build();
    }
}
