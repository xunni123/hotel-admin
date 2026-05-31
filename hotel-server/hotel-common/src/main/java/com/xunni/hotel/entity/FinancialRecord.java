package com.xunni.hotel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("financial_record")
public class FinancialRecord {
    @TableId(value = "record_id", type = IdType.AUTO)
    private Integer recordId;

    @TableField("type")
    private String type;

    @TableField("amount")
    private BigDecimal amount;

    @TableField("payment_method")
    private String paymentMethod;

    @TableField("order_no")
    private String orderNo;

    @TableField("remark")
    private String remark;

    @TableField("operator")
    private String operator;

    @TableField("create_time")
    private LocalDateTime createTime;
}
