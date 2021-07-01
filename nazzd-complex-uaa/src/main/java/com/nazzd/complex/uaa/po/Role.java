package com.nazzd.complex.uaa.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(value = "t_role")
public class Role {

    private Long id;

    private String roleName;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
