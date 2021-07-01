package com.nazzd.complex.gateway.service;

import com.nazzd.common.exception.ClientException;
import com.nazzd.complex.gateway.bo.Login;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.oauth2.OAuth2ClientProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;


@Slf4j
@Service
public class OAuth2AuthenticationService {

    private static final String OAUTH_TOKEN = "http://nazzd-complex-uaa/oauth/token";


    @Resource
    private OAuth2ClientProperties oAuth2ClientProperties;

    @Resource
    private RestTemplate restTemplate;

    public OAuth2AccessToken authenticate(Login login) {
        try {
            String username = login.getUsername();
            String password = login.getPassword();
            HttpHeaders reqHeaders = new HttpHeaders();
            reqHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> formParams = new LinkedMultiValueMap<>();
            formParams.set("client_id", oAuth2ClientProperties.getClientId());
            formParams.set("client_secret", oAuth2ClientProperties.getClientSecret());
            formParams.set("username", username);
            formParams.set("password", password);
            formParams.set("grant_type", "password");
            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(formParams, reqHeaders);
            log.debug("contacting OAuth2 token endpoint to login user: {}", username);
            ResponseEntity<OAuth2AccessToken>
                    responseEntity = restTemplate.postForEntity(OAUTH_TOKEN, entity, OAuth2AccessToken.class);
            if (log.isDebugEnabled()) {
                log.debug("successfully authenticated user {}", username);
            }
            return responseEntity.getBody();
        } catch (HttpClientErrorException ex) {
            log.warn("failed to get OAuth2 tokens from UAA", ex);
            throw new ClientException("您的手机号/密码输入错误");
        }
    }


    /**
     * 刷新token
     */
    public OAuth2AccessToken refreshToken(String refreshTokenValue) {
        HttpHeaders httpheaders = new HttpHeaders();
        httpheaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> formParams = new LinkedMultiValueMap<>();
        formParams.set(OAuth2Utils.CLIENT_ID, oAuth2ClientProperties.getClientId());
        formParams.set("client_secret", oAuth2ClientProperties.getClientSecret());
        formParams.set(OAuth2Utils.GRANT_TYPE, OAuth2AccessToken.REFRESH_TOKEN);
        formParams.set(OAuth2AccessToken.REFRESH_TOKEN, refreshTokenValue);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(formParams, httpheaders);
        ResponseEntity<OAuth2AccessToken>
                responseEntity = restTemplate.postForEntity(OAUTH_TOKEN, entity, OAuth2AccessToken.class);
        return responseEntity.getBody();
    }

}

