package com.nazzd.common.feign;

import org.springframework.context.annotation.Bean;

public class OAuth2UserClientFeignConfiguration {

    /**
     * 传递的token是登录信息里的
     */
    @Bean
    public UserFeignClientInterceptor userFeignClientInterceptor() {
        return new UserFeignClientInterceptor();
    }

}

