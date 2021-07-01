package com.nazzd.complex.uaa.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_user")
public class User {

    private Long id;

    private String username;

    @JsonIgnore
    private String password;

    private String phone;

    private Boolean isEnabled;
    
    private Boolean isDeleted;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
