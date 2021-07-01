package com.nazzd.common.security;

import org.springframework.boot.autoconfigure.security.oauth2.resource.JwtAccessTokenConverterConfigurer;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

/**
 * 设置了自定义的用户认证信息转换器
 */
public class CustomJwtAccessTokenConverterConfigurer implements JwtAccessTokenConverterConfigurer {

    private final CustomUserAuthenticationConverter customUserAuthenticationConverter;

    public CustomJwtAccessTokenConverterConfigurer(CustomUserAuthenticationConverter customUserAuthenticationConverter) {
        this.customUserAuthenticationConverter = customUserAuthenticationConverter;
    }

    @Override
    public void configure(JwtAccessTokenConverter converter) {
        AccessTokenConverter accessTokenConverter = converter.getAccessTokenConverter();
        ((DefaultAccessTokenConverter) accessTokenConverter).setUserTokenConverter(customUserAuthenticationConverter);
    }

}

