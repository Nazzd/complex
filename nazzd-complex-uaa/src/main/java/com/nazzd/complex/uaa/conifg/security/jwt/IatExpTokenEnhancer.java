package com.nazzd.complex.uaa.conifg.security.jwt;

import java.time.Instant;
import java.time.temporal.ChronoField;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

/**
 * Adds the standard "iat" claim to tokens so we know when they have been created.
 * This is needed for a session timeout due to inactivity (ignored in case of "remember-me").
 */
@Component
public class IatExpTokenEnhancer implements TokenEnhancer {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        addClaims((DefaultOAuth2AccessToken) accessToken);
        return accessToken;
    }

    private void addClaims(DefaultOAuth2AccessToken accessToken) {
        Map<String, Object> additionalInformation = accessToken.getAdditionalInformation();
        if (additionalInformation.isEmpty()) {
            additionalInformation = new LinkedHashMap<>();
        }
        // add "iat" claim with current time in secs
        // this is used for an inactive session timeout
        additionalInformation.put("iat", Instant.now().getLong(ChronoField.INSTANT_SECONDS));

        Date expiration = accessToken.getExpiration();
        if (Objects.nonNull(expiration)) {
            additionalInformation.put(AccessTokenConverter.EXP, expiration.toInstant().getLong(ChronoField.INSTANT_SECONDS));
        }
        accessToken.setAdditionalInformation(additionalInformation);
    }

}

