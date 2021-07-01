package com.nazzd.complex.uaa.conifg.security;

import com.google.common.collect.ImmutableList;
import com.nazzd.complex.uaa.conifg.security.jwt.IatExpTokenEnhancer;
import org.springframework.boot.autoconfigure.security.oauth2.authserver.AuthorizationServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.security.KeyPair;

@Configuration
@EnableAuthorizationServer
@EnableConfigurationProperties(AuthorizationServerProperties.class)
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    @Resource
    private AuthorizationServerProperties authorizationServerProperties;

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private UsernameUserDetailsService usernameUserDetailsService;

    @Resource
    private IatExpTokenEnhancer iatExpTokenEnhancer;

    @Resource
    private PasswordEncoder passwordEncoder;


    @Resource
    private DataSource dataSource;

    @Bean
    public DefaultAccessTokenConverter defaultAccessTokenConverter() {
        DefaultAccessTokenConverter defaultAccessTokenConverter = new DefaultAccessTokenConverter();
        defaultAccessTokenConverter.setUserTokenConverter(new UaaUserAuthenticationConverter());
        return defaultAccessTokenConverter;
    }


    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        AuthorizationServerProperties.Jwt jwtProperties = authorizationServerProperties.getJwt();
        KeyPair keyPair = new KeyStoreKeyFactory(
                new ClassPathResource(jwtProperties.getKeyStore()),
                jwtProperties.getKeyPassword().toCharArray()).getKeyPair(jwtProperties.getKeyAlias());
        converter.setKeyPair(keyPair);
        converter.setAccessTokenConverter(defaultAccessTokenConverter());
        return converter;
    }

    /**
     * 存储令牌
     */
    @Bean
    public JwtTokenStore jwtTokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }


    /**
     * 配置AuthorizationServer安全认证的相关信息
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        // 支持将client参数放在header或body中
        security
                .tokenKeyAccess(authorizationServerProperties.getTokenKeyAccess())
                .checkTokenAccess(authorizationServerProperties.getCheckTokenAccess())
                .allowFormAuthenticationForClients();
    }

    /**
     * 配置OAuth2的客户端相关信息
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 配置客户端信息，从数据库中读取，对应oauth_client_details表
        clients.jdbc(dataSource);
        /*clients.inMemory()
                .withClient("internal")//配置client_id
                .secret(passwordEncoder.encode("internal"))//配置client_secret
                .accessTokenValiditySeconds(3600)//配置访问token的有效期
                .refreshTokenValiditySeconds(864000)//配置刷新token的有效期
                .redirectUris("http://www.baidu.com")//配置redirect_uri，用于授权成功后跳转
                .scopes("all")//配置申请的权限范围
                .authorizedGrantTypes("authorization_code", "password", "refresh_token");//配置grant_type，表示授权类型*/
    }

    /**
     * 配置AuthorizationServerEndpointsConfigurer众多相关类
     * 包括配置身份认证器，配置认证方式，TokenStore，TokenGranter，OAuth2RequestFactory
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        // 配置token的数据源、自定义的tokenServices等信息,配置身份认证器，配置认证方式，TokenStore，TokenGranter，OAuth2RequestFactory
        /**
         * jwt内容增强
         */
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(ImmutableList.of(jwtAccessTokenConverter(), iatExpTokenEnhancer));

        endpoints
                .authenticationManager(authenticationManager)//开启密码授权类型
                .userDetailsService(usernameUserDetailsService)//要使用refresh_token的话，需要额外配置userDetailsService
                .tokenStore(jwtTokenStore())//配置token存储方式
                .tokenEnhancer(tokenEnhancerChain)
                // refresh_token有两种使用方式：重复使用(true)、非重复使用(false)，默认为true
                //  1.重复使用：access_token过期刷新时， refresh token过期时间未改变，仍以初次生成的时间为准
                //  2.非重复使用：access_token过期刷新时， refresh_token过期时间延续，在refresh_token有效期内刷新而无需失效再次登录
                .reuseRefreshTokens(false);
    }

}
