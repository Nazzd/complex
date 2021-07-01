package com.nazzd.common.feign;

import com.nazzd.common.security.LoadBalancedClientCredentialsResourceDetails;
import org.springframework.cloud.security.oauth2.client.feign.OAuth2FeignRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;


import feign.RequestInterceptor;

public class OAuth2InterceptedFeignConfiguration {

    /**
     * 客户端登录获取token
     */
    @Bean
    public RequestInterceptor oauth2FeignRequestInterceptor(
            LoadBalancedClientCredentialsResourceDetails loadBalancedClientCredentialsResourceDetails) {
        return new OAuth2FeignRequestInterceptor(new DefaultOAuth2ClientContext(), loadBalancedClientCredentialsResourceDetails);
    }

}
