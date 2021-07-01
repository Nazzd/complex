package com.nazzd.complex.gateway.security;

import com.nazzd.complex.gateway.filter.RefreshTokenFilter;

import javax.servlet.http.HttpServletRequest;


public class RefreshTokenExtractor {

    /**
     * Extract a refresh token value from an incoming request
     *
     * @param request the current ServletRequest
     * @return refresh token
     */
    public String extract(HttpServletRequest request) {
        return request.getHeader(RefreshTokenFilter.REFRESH_TOKEN);
    }

}
