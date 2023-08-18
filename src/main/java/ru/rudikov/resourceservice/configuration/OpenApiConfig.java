package ru.rudikov.resourceservice.configuration;

import static io.swagger.v3.oas.annotations.enums.SecuritySchemeType.HTTP;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(name = "bearerAuth", type = HTTP, bearerFormat = "JWT", scheme = "bearer")
@OpenAPIDefinition(
    info = @Info(title = "Resource service", version = "1.0", description = "User management"))
public class OpenApiConfig {}
