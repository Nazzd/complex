package com.nazzd.complex.gateway.filter;

import com.nazzd.complex.gateway.service.OAuth2AuthenticationService;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationProcessingFilter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class RefreshTokenFilterConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Resource
    private OAuth2AuthenticationService oAuth2AuthenticationService;

    @Resource
    private TokenStore tokenStore;

    @Override
    public void configure(HttpSecurity http) {
        RefreshTokenFilter refreshTokenFilter = new RefreshTokenFilter(oAuth2AuthenticationService, tokenStore);
        http.addFilterBefore(refreshTokenFilter, OAuth2AuthenticationProcessingFilter.class);
    }

}
