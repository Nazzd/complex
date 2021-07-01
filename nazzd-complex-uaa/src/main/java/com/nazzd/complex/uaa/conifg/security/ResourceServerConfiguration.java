package com.nazzd.complex.uaa.conifg.security;


import com.nazzd.common.security.CustomJwtAccessTokenConverterConfigurer;
import com.nazzd.common.security.CustomUserAuthenticationConverter;
import com.nazzd.common.security.LoadBalancedJwtAccessTokenConverterRestTemplateCustomizer;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.cloud.client.loadbalancer.RetryLoadBalancerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Resource
    private JwtTokenStore jwtTokenStore;

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
        resources.resourceId(resourceServerProperties.getResourceId()).tokenStore(jwtTokenStore);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                /*.apply(weixinAuthenticationSecurityConfig)
                .and()
                .apply(phoneAuthenticationSecurityConfig)
                .and()*/
                .exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                .and()
                .csrf()//关闭跨站请求防护
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()//配置路径拦截
                // 允许所有的Endpoint端点链接
                .requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
                .antMatchers("/api/verification-code/**").permitAll() // 验证码
                .antMatchers("/api/account/reset-password/**").permitAll() // 重置密码
                .antMatchers("/api/**").authenticated();
    }


}
