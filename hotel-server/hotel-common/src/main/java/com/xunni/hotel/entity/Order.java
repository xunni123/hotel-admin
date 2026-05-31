package com.xunni.hotel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("orders")
public class Order {
    @TableId(value = "order_id", type = IdType.AUTO)
    private Integer orderId;

    @TableField("order_no")
    private String orderNo;

    @TableField("room_number")
    private String roomNumber;

    @TableField("room_id")
    private Integer roomId;

    @TableField("guest_name")
    private String guestName;

    @TableField("guest_phone")
    private String guestPhone;

    @TableField("check_in_date")
    private String checkInDate;

    @TableField("check_out_date")
    private String checkOutDate;

    @TableField("nights")
    private Integer nights;

    @TableField("total_amount")
    private BigDecimal totalAmount;

    @TableField("payment_method")
    private String paymentMethod;

    @TableField("order_status")
    private String orderStatus;

    @TableField("payment_status")
    private String paymentStatus;

    @TableField("book_channel")
    private String bookChannel;

    @TableField("remarks")
    private String remarks;

    @TableField("create_time")
    private LocalDateTime createTime;
}