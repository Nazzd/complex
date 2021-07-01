package com.nazzd.complex.uaa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nazzd.complex.uaa.po.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
