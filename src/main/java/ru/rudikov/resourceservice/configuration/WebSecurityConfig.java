package ru.rudikov.resourceservice.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.rudikov.resourceservice.application.service.auth.JwtFilter;

import static org.springframework.http.HttpMethod.GET;
import static ru.rudikov.resourceservice.application.domain.model.dto.Role.ADMIN;
import static ru.rudikov.resourceservice.application.domain.model.dto.Role.USER;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagementCustomizer ->
                        sessionManagementCustomizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(authorizeHttpRequestsCustomizer ->
                        authorizeHttpRequestsCustomizer
                                .requestMatchers("/api/auth/login", "/api/auth/token").permitAll()
                                .requestMatchers(GET, "/resource/**").hasAnyAuthority(
                                        USER.getAuthority(), ADMIN.getAuthority()
                                )
                                .requestMatchers("/resource/**").hasAuthority(ADMIN.getAuthority())
                                .anyRequest().authenticated()

                )
                .addFilterAfter(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
