package com.nazzd.complex.gateway.filter;

import com.google.common.collect.Iterators;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Collections;
import java.util.Enumeration;

public class HeadersHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private final Enumeration<String> authorizationHeader;

    public HeadersHttpServletRequestWrapper(HttpServletRequest request, String accessToken) {
        super(request);
        String authorization = OAuth2AccessToken.BEARER_TYPE + " " + accessToken;
        authorizationHeader = Iterators.asEnumeration(Collections.singletonList(authorization).iterator());
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        // BearerTokenExtractor:73 extractHeaderToken(HttpServletRequest request)
        // 重写getHeaders，针对OAuth2AuthenticationProcessingFilter中获取Header取出token
        if (HttpHeaders.AUTHORIZATION.equals(name)) {
            return authorizationHeader;
        }
        return super.getHeaders(name);
    }

}
