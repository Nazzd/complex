package com.nazzd.complex.uaa.conifg.security;

import com.nazzd.complex.uaa.po.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class CustomUserDetailsComponent {


    public CustomUserDetails getCustomUserDetails(User user) {
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        // 获取用户角色
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        CustomUserDetails customUserDetails = new CustomUserDetails(user, grantedAuthorities);
        customUserDetails.setRoleName("ADMIN");
        return customUserDetails;
    }
}
