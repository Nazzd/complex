package com.nazzd.complex.uaa.conifg.security;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.nazzd.complex.uaa.mapper.UserMapper;
import com.nazzd.complex.uaa.po.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 自定义用户数据校验
 */
@Slf4j
@Component
public class UsernameUserDetailsService implements UserDetailsService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private CustomUserDetailsComponent customUserDetailsComponent;

    @Resource
    private PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<User> queryWrapper = Wrappers.<User>lambdaQuery().eq(User::getUsername, username);
        User user = userMapper.selectOne(queryWrapper);
        log.info(user.getPassword());
        return customUserDetailsComponent.getCustomUserDetails(user);
    }
}
