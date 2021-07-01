package com.nazzd.common.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.autoconfigure.security.oauth2.resource.JwtAccessTokenConverterRestTemplateCustomizer;
import org.springframework.cloud.client.loadbalancer.RetryLoadBalancerInterceptor;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

/**
 * 获取token_key时, 负载均衡
 */
public class LoadBalancedJwtAccessTokenConverterRestTemplateCustomizer implements JwtAccessTokenConverterRestTemplateCustomizer {

    private final RetryLoadBalancerInterceptor loadBalancerInterceptor;

    public LoadBalancedJwtAccessTokenConverterRestTemplateCustomizer(RetryLoadBalancerInterceptor loadBalancerInterceptor) {
        this.loadBalancerInterceptor = loadBalancerInterceptor;
    }

    @Override
    public void customize(RestTemplate template) {
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>(template.getInterceptors());
        interceptors.add(loadBalancerInterceptor);
        template.setInterceptors(interceptors);
    }

}

