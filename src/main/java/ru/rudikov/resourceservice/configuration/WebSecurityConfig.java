package ru.rudikov.resourceservice.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import ru.rudikov.resourceservice.application.service.auth.JwtFilter;

import static org.springframework.http.HttpMethod.GET;
import static ru.rudikov.resourceservice.application.domain.model.dto.Role.ADMIN;
import static ru.rudikov.resourceservice.application.domain.model.dto.Role.USER;

@Slf4j
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
                .exceptionHandling(exceptionHandlingSpec -> {
                            exceptionHandlingSpec.authenticationEntryPoint((exchange, ex) -> {
                                        log.error("UNAUTHORIZED", ex);
                                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                                        return exchange.getResponse().setComplete();
                                    }
                            );

                            exceptionHandlingSpec.accessDeniedHandler((exchange, ex) -> {
                                log.error("FORBIDDEN", ex);
                                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                                return exchange.getResponse().setComplete();
                            });
                        }
                )
                .authorizeExchange(authorizeExchangeCustomizer ->
                        authorizeExchangeCustomizer
                                .pathMatchers("/api/auth/login", "/api/auth/token").permitAll()
                                .pathMatchers(GET, "/resource/**").hasAnyAuthority(
                                        USER.getAuthority(), ADMIN.getAuthority()
                                )
                                .pathMatchers("/resource/**").hasAuthority(ADMIN.getAuthority())
                                .anyExchange().authenticated()
                )
                .addFilterAt(jwtFilter, SecurityWebFiltersOrder.AUTHENTICATION);

        return http.build();
    }
}
