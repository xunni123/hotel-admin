package com.xunni.hotel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("member_consume")
public class MemberConsume {
    @TableId(value = "consume_id", type = IdType.AUTO)
    private Integer consumeId;

    @TableField("member_id")
    private Integer memberId;

    @TableField("member_no")
    private String memberNo;

    @TableField("member_name")
    private String memberName;

    @TableField("phone")
    private String phone;

    @TableField("type")
    private String type;

    @TableField("amount")
    private BigDecimal amount;

    @TableField("points_change")
    private Integer pointsChange;

    @TableField("current_points")
    private Integer currentPoints;

    @TableField("remark")
    private String remark;

    @TableField("operator")
    private String operator;

    @TableField("create_time")
    private LocalDateTime createTime;
}
