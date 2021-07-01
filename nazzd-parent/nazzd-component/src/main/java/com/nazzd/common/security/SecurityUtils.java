package com.nazzd.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;
import java.util.Optional;

public class SecurityUtils {

    private static final String ID = "id";
    private static final String USERNAME = "username";
    private static final String PHONE = "phone";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();


    private SecurityUtils() {
    }

    /**
     * 获取当前登录用户信息
     */
    @SuppressWarnings("unchecked")
    public static Optional<Map<String, Object>> getCurrentUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
                .map(authentication -> {
                    // 各个微服务会取到Map
                    if (authentication.getPrincipal() instanceof Map) {
                        return (Map<String, Object>) authentication.getPrincipal();
                    }
                    // UAA设置了UserDetailService，会取到CustomUserDetails
                    else if (authentication.getPrincipal() instanceof UserDetails) {
                        return OBJECT_MAPPER.convertValue(authentication.getPrincipal(), Map.class);
                    }
                    return null;
                });
    }

    /**
     * 获取当前登录用户id
     */
    public static Long getCurrentUserId() {
        return getCurrentUser()
                .map(user -> user.get(ID))
                .map(object -> Long.valueOf(object.toString()))
                .orElse(null);
    }

    /**
     * 获取当前登录用户登录名
     */
    public static String getCurrentUserUsername() {
        return getCurrentUser()
                .map(user -> user.get(USERNAME))
                .map(Object::toString)
                .orElse(null);
    }

    /**
     * 获取当前登录用户姓名
     */
    public static String getCurrentUserPhone() {
        return getCurrentUser()
                .map(user -> user.get(PHONE))
                .map(Object::toString)
                .orElse(null);
    }

}
