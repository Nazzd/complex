package com.nazzd.complex.uaa.service.impl;

import com.nazzd.complex.uaa.mapper.UserMapper;
import com.nazzd.complex.uaa.po.User;
import com.nazzd.complex.uaa.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public User getUser(Long userId) {
        return userMapper.selectById(userId);
    }

}
