package com.nazzd.complex.gateway.config;

import com.nazzd.complex.gateway.filter.RefreshTokenFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 重写 WebMvcConfigurer(全局跨域)
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    /**
     * 跨域设置
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(CorsConfiguration.ALL)// 放行哪些原始域
                .allowedMethods(CorsConfiguration.ALL)// 放行哪些请求方法
                .allowedHeaders(CorsConfiguration.ALL)// 放行哪些原始请求头部
                .exposedHeaders(HttpHeaders.CONTENT_DISPOSITION, RefreshTokenFilter.ACCESS_TOKEN, RefreshTokenFilter.REFRESH_TOKEN);// 暴露哪些头部信息
    }

}

