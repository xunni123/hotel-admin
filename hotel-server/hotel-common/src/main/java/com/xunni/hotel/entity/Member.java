package com.xunni.hotel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("members")
public class Member {
    @TableId(value = "member_id", type = IdType.AUTO)
    private Integer memberId;
    
    @TableField("member_no")
    private String memberNo;
    
    @TableField("name")
    private String name;
    
    @TableField("phone")
    private String phone;
    
    @TableField("email")
    private String email;
    
    @TableField("level")
    private String level;
    
    @TableField("points")
    private Integer points;
    
    @TableField("register_date")
    private String registerDate;
    
    @TableField("status")
    private String status;
    
    @TableField("total_consumption")
    private Float totalConsumption;
    
    @TableField("last_consumption_date")
    private String lastConsumptionDate;
}