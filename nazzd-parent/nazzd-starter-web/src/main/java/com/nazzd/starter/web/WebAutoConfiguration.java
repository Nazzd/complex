package com.nazzd.starter.web;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Web自动配置类
 */
@Configuration
@EnableConfigurationProperties(WebProperties.class)
public class WebAutoConfiguration {

    /**
     * Web配置, 主要使用了code message data的格式封装及没有使用http状态码响应
     */
    @Configuration
    @ConditionalOnProperty(prefix = WebProperties.PREFIX, name = "response-body-advice", havingValue = "true")
    public static class CustomResponseBodyConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public RestExceptionHandler restExceptionHandler() {
            return new RestExceptionHandler();
        }

        @Bean
        @ConditionalOnMissingBean
        public RestResponseBodyAdvice restResponseBodyAdvice() {
            return new RestResponseBodyAdvice();
        }

    }

    /**
     * Web配置, 主要使用了http状态码响应以及直接返回正确响应结果, 不包装code message
     */
    @Configuration
    @ConditionalOnProperty(prefix = WebProperties.PREFIX, name = "response-body-advice", havingValue = "false", matchIfMissing = true)
    public static class NewConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public RestResponseEntityExceptionHandler restResponseEntityExceptionHandler() {
            return new RestResponseEntityExceptionHandler();
        }

    }

    @Bean
    @ConditionalOnProperty(prefix = WebProperties.PREFIX, name = "trim-string", matchIfMissing = true)
    public WebDataBinderControllerAdvice webDataBinderControllerAdvice() {
        return new WebDataBinderControllerAdvice();
    }

}
