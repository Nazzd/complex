package com.nazzd.complex.training.conifg;


import com.nazzd.common.security.CustomJwtAccessTokenConverterConfigurer;
import com.nazzd.common.security.CustomUserAuthenticationConverter;
import com.nazzd.common.security.LoadBalancedJwtAccessTokenConverterRestTemplateCustomizer;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.cloud.client.loadbalancer.RetryLoadBalancerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

import javax.annotation.Resource;


@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Resource
    private ResourceServerProperties resourceServerProperties;

    @Bean
    public LoadBalancedJwtAccessTokenConverterRestTemplateCustomizer loadBalancedJwtAccessTokenConverterRestTemplateCustomizer(
            RetryLoadBalancerInterceptor loadBalancerInterceptor) {
        return new LoadBalancedJwtAccessTokenConverterRestTemplateCustomizer(
                loadBalancerInterceptor);
    }

    /**
     * 自定义用户认证信息转换器
     */
    @Bean
    public CustomUserAuthenticationConverter customUserAuthenticationConverter() {
        return new CustomUserAuthenticationConverter();
    }

    @Bean
    public CustomJwtAccessTokenConverterConfigurer customJwtAccessTokenConverterConfigurer() {
        return new CustomJwtAccessTokenConverterConfigurer(customUserAuthenticationConverter());
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(resourceServerProperties.getResourceId());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .headers()
                .frameOptions()
                .disable()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // 允许所有的Endpoint端点链接
                .requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .antMatchers("/openapi/**").permitAll()
                .antMatchers("/api/**").authenticated();
    }

}


