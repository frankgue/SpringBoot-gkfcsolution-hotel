package com.gkfcsolution.gkfcsolutionhotel.config;


import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created on 2025 at 11:05
 * File: null.java
 * Project: springboot-movies-bookinks
 *
 * @author Frank GUEKENG
 * @date 12/12/2025
 * @time 11:05
 */

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI myApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Hotel Booking Hotel APP GKFCSOLUTION REST API")
                        .description("Documentation des API Spring Boot")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Frank GUEKENG")
                                .email("gkfcsolutions@gmail.com")
                                .url("https://gkfcsolutions.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("Documentation complète")
                        .url("https://swagger.io"));
    }
}
