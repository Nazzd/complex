package com.nazzd.complex.gateway.filter;

import com.nazzd.complex.gateway.security.RefreshTokenExtractor;
import com.nazzd.complex.gateway.service.OAuth2AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * 刷新token过滤器
 */
@Slf4j
public class RefreshTokenFilter extends GenericFilterBean {

    // 距离过期时间剩余的秒数
    private static final int REFRESH_WINDOW_SECS = 30;

    public static final String REFRESH_TOKEN = "Refresh-Token";
    public static final String ACCESS_TOKEN = "Access-Token";

    private final TokenExtractor tokenExtractor = new BearerTokenExtractor();

    private final RefreshTokenExtractor refreshTokenExtractor = new RefreshTokenExtractor();

    private final OAuth2AuthenticationService oAuth2AuthenticationService;
    private final TokenStore tokenStore;

    public RefreshTokenFilter(OAuth2AuthenticationService oAuth2AuthenticationService, TokenStore tokenStore) {
        this.oAuth2AuthenticationService = oAuth2AuthenticationService;
        this.tokenStore = tokenStore;
    }

    /**
     * Check access token and refresh it, if it is either not present, expired or about to expire.
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        try {
            httpServletRequest = refreshTokensIfExpiring(httpServletRequest, httpServletResponse);
        } catch (Exception e) {
            log.warn("Security exception: could not refresh tokens", e);
            ((HttpServletResponse) servletResponse).setStatus(401);
            return;
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private HttpServletRequest refreshTokensIfExpiring(HttpServletRequest request, HttpServletResponse response) {
        // 取出请求头中的token，如果携带token则查看是否过期，过期了就刷新token,否则不做处理
        Authentication authentication = tokenExtractor.extract(request);
        if (Objects.isNull(authentication)) {
            return request;
        }
        String token = (String) authentication.getPrincipal();
        // 读取token中的信息
        OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(token);
        // token过期或者即将过期
        if (mustRefreshToken(oAuth2AccessToken)) {
            // 取出refreshToken
            String refreshToken = refreshTokenExtractor.extract(request);
            if (Objects.isNull(refreshToken)) {
                log.warn("Refresh token not found");
            } else {
                log.info("调用uaa刷新token, refresh-token: {}", refreshToken);
                OAuth2AccessToken authenticate = oAuth2AuthenticationService.refreshToken(refreshToken);
                String accessToken = authenticate.getValue();
                request = new HeadersHttpServletRequestWrapper(request, accessToken);
                response.setHeader(ACCESS_TOKEN, accessToken);
                response.setHeader(REFRESH_TOKEN, authenticate.getRefreshToken().getValue());
            }
        }
        return request;
    }

    private boolean mustRefreshToken(OAuth2AccessToken accessToken) {
        // check if token is expired or about to expire
        return (accessToken.isExpired() ||
                (0 < accessToken.getExpiresIn() && accessToken.getExpiresIn() < REFRESH_WINDOW_SECS));
    }

}

