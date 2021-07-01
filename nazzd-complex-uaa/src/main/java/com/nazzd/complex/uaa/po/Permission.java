package com.nazzd.complex.uaa.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(value = "t_permission")
public class Permission {

    private Long id;

    private String permissionName;

    private String url;

    private Long parentId;

    private String description;

    private Integer type;

    private Boolean isLeaf;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
