package com.xunni.hotel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("users")
public class User {
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;
    private String username;
    private String password;
    @TableField("real_name")
    private String realName;
    private String avatar;
    private String status;
    private Long phone;
    private String email;
    @TableField(exist = false)
    private Long roleId;
}
