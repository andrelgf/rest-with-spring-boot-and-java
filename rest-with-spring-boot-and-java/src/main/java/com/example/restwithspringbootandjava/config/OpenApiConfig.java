package com.example.restwithspringbootandjava.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Rest with Java and Spring")
                        .version(null)
                        .description(null)
                        .termsOfService(null)
                        .license(new License().name("Apache 2.0")
                                .url(null)));
    }
}
