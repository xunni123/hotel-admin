package com.xunni.hotel.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("roles")
public class Role {
    @TableId(value = "role_id")
    private Long roleId;
    private String roleName;
}
