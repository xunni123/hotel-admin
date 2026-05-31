package com.xunni.hotel.entity.HomeCard;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class Orders {
    private Integer orderId;
    private String orderNo;
    private Integer memberId;
    private Integer roomId;
    private String roomNumber;
    private String guestName;
    private String guestPhone;
    private String idCard;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Integer nights;
    private Integer adults;
    private Integer children;
    private BigDecimal totalAmount;
    private BigDecimal paidAmount;
    private BigDecimal depositAmount;
    private String orderStatus;     // 如 'reserved','checked_in','checked_out','cancelled'
    private String paymentStatus;
    private String paymentMethod;
    private String region;          // 客源地区，如 '北京', '上海', '广东' 等
    private String bookChannel;     // 预订渠道，如 '官网', '携程', '美团', '电话' 等
    private String customerType;    // 客户类型，如 '散客', '团体', '会员' 等
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

}
