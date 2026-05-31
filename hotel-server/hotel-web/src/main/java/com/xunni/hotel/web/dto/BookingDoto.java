package com.xunni.hotel.web.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class BookingDoto {
    private Integer orderId;
    private String orderNo;
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
    private String orderStatus;
    private String paymentStatus;
    private String paymentMethod;
    private String source;
    private String bookChannel;
    private String checkInType;
    private String remarks;
    private String createBy;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String region;
    private String customerType;

}