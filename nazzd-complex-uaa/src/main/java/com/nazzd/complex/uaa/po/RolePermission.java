package com.nazzd.complex.uaa.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "t_role_permission")
public class RolePermission {

    private Long id;

    private Long roleId;

    private Long permissionId;
}
