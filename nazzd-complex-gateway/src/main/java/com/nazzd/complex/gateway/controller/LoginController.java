package com.nazzd.complex.gateway.controller;

import com.nazzd.complex.gateway.bo.Login;
import com.nazzd.complex.gateway.service.OAuth2AuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Tag(name = "登录")
@Slf4j
@Validated
@RestController
@RequestMapping("/auth")
public class LoginController {

    @Resource
    private OAuth2AuthenticationService authenticationService;

    @PostMapping("/login")
    public OAuth2AccessToken login(@RequestBody @Validated Login loginVM) {
        return authenticationService.authenticate(loginVM);
    }


    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout() {
        log.info("logging out user {}", SecurityContextHolder.getContext().getAuthentication().getName());
    }

}

