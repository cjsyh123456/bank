package com.example.bank.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI bankTransactionOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Bank Transaction Management API")
                        .description("API for managing banking transactions")
                        .version("1.0")
                        .contact(new Contact()
                                .name("API Support")
                                .email("xxx@test.com")));
    }
}