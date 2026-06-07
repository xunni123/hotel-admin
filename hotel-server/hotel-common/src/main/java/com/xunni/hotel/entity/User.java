package com.xunni.hotel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xunni.hotel.annotation.Sensitive;

import lombok.Data;

@Data
@TableName("users")
public class User {
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;
    
    private String username;
    
    // 密码不返回给前端
    @TableField(exist = false)
    private String password;
    
    @Sensitive(type = Sensitive.SensitiveType.NAME)
    private String realName;
    
    private String avatar;
    
    private String status;
    
    @Sensitive(type = Sensitive.SensitiveType.PHONE)
    private String phone;
    
    @Sensitive(type = Sensitive.SensitiveType.EMAIL)
    private String email;
    
    @TableField(exist = false)
    private Long roleId;
}