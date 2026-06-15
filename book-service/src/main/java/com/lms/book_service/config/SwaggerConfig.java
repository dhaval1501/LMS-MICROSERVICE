package com.lms.book_service.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI bookServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Book Service API")
                        .description("APIs for managing books")
                        .version("v1.0"));
    }
}

