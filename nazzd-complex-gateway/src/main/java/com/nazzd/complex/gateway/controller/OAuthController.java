package com.nazzd.complex.gateway.controller;

import com.nazzd.common.exception.ClientException;
import com.nazzd.complex.gateway.security.RefreshTokenExtractor;
import com.nazzd.complex.gateway.service.OAuth2AuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Tag(name = "OAuth")
@Slf4j
@RestController
@RequestMapping("/oauth")
public class OAuthController {

    private final RefreshTokenExtractor refreshTokenExtractor = new RefreshTokenExtractor();

    @Resource
    private OAuth2AuthenticationService authenticationService;

    @PostMapping("/refresh-token")
    public OAuth2AccessToken refreshToken(HttpServletRequest request) {
        String refreshToken = refreshTokenExtractor.extract(request);
        if (StringUtils.isBlank(refreshToken)) {
            throw new ClientException("Refresh Token不能为空");
        }
        log.info("refresh token, token={}", refreshToken);
        return authenticationService.refreshToken(refreshToken);
    }

}

