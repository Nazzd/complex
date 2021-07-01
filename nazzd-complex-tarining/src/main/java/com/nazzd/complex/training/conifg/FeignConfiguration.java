package com.nazzd.complex.training.conifg;

import com.nazzd.common.feign.UserFeignClientInterceptor;
import com.nazzd.common.security.LoadBalancedClientCredentialsResourceDetails;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class FeignConfiguration {

    @Bean
    @ConfigurationProperties("security.oauth2.client")
    public LoadBalancedClientCredentialsResourceDetails loadBalancedClientCredentialsResourceDetails(
            LoadBalancerClient loadBalancerClient) {
        return new LoadBalancedClientCredentialsResourceDetails(loadBalancerClient);
    }

    @Bean
    public UserFeignClientInterceptor userFeignClientInterceptor() {
        return new UserFeignClientInterceptor();
    }

}
