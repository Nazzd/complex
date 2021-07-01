package com.nazzd.complex.gateway.config;

import java.util.Optional;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SpringDocConfiguration {

    @Bean
    public OpenAPI openAPI(@Value("${spring.application.name}") String applicationName,
                           ObjectProvider<BuildProperties> buildProperties) {
        return new OpenAPI()
                .info(new Info().title(applicationName)
                        .description("网关")
                        .version(Optional.ofNullable(buildProperties.getIfAvailable())
                                .map(BuildProperties::getVersion)
                                .orElse("1.0.0")
                        )
                );
    }

}

