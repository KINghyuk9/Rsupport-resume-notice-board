package com.example.rsupport.noticeboard.config;

import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import io.swagger.v3.oas.models.OpenAPI;

@Configuration
@EnableWebSecurity
public class SwaggerConfig {

    private final String port = "8080";

    @Bean
    public OpenAPI openAPI() {
        String key = "authorization key";
        return new OpenAPI()
                .components(components(key))
                .addSecurityItem(securityRequirement(key))
                .addServersItem(new Server().description("로컬").url("http://localhost:" + port))
                .info(apiInfo());
    }

    private Components components(String key) {
        return new Components()
                .addSecuritySchemes(key, new SecurityScheme()
                        .name(key)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT"));
    }
    private SecurityRequirement securityRequirement(String key) {
        return new SecurityRequirement().addList(key);
    }
    private Info apiInfo() {
        return new Info()
                .title("Rsupport Noticeboard")
                .description("Rsupport Noticeboard API 문서")
                .version("1.0.0");
    }
}

