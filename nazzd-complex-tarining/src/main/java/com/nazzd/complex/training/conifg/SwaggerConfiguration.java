package com.nazzd.complex.training.conifg;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.Optional;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI openApi(@Value("${spring.application.name}") String applicationName,
                           ObjectProvider<BuildProperties> buildProperties) {
        // 接口调试路径
        Server tryServer = new Server();
        tryServer.setUrl("http://localhost:40000/training");

        return new OpenAPI()
                .components(new Components().addSecuritySchemes("bearer-jwt", // key值
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP) //请求认证类型
                                .scheme("bearer").bearerFormat("JWT")
                                .name("Authorization") //API key参数名
                                .description("token令牌") //API key描述
                                .in(SecurityScheme.In.HEADER))) //设置API key的存放位置
                //.addSecurityItem(new SecurityRequirement().addList("bearer-jwt", Arrays.asList("read", "write")))
                .info(new Info().title(applicationName)
                        .description("模型训练")
                        .version(Optional.ofNullable(buildProperties.getIfAvailable())
                                .map(BuildProperties::getVersion)
                                .orElse("1.0.0")
                        )
                ).servers(Collections.singletonList(tryServer));
    }

}

