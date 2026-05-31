package com.xunni.hotel.web.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.lang.reflect.Array;

@Data
public class RoomDoto {
    // 房间字段
    private Integer roomId;
    private String roomNumber;
    private String building;
    private String floor;
    private String roomType;
    private BigDecimal price;
    private Integer capacity;
    private String status;
    private String statusName;
    private String statusColor;
    private Array features;

    // 搜索参数（用于查询）
    private String searchText;
    private Boolean idle;
    private Boolean dirty;
    private Boolean repair;
    private Boolean booked;
    private Boolean checkedIn;
    private Boolean locked;
    private Boolean selfUse;
    private Boolean todayCheckout;
    private String checkinType;
    private String channel;
    private Array specialTags;

    // 订单字段
    private Integer orderId;
    private String orderNo;
    private String guestName;
    private String guestPhone;
    private String idCard;
    private LocalDate checkInTime;
    private LocalDate checkOutTime;
    private Integer nights;
    private BigDecimal totalAmount;
    private String orderStatus;
    private String paymentStatus;
    private String paymentMethod;
    private String bookChannel;
    private Integer currentGuests;
}
