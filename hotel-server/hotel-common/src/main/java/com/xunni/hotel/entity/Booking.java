package com.xunni.hotel.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("orders")
public class Booking {
    @TableId
    private Integer orderId;
    private String orderNo;
    private String roomNumber;
    private String guestName;
    private String guestPhone;
    private String checkInDate;
    private String checkOutDate;
    private Integer nights;
    private Float totalAmount;
    private String orderStatus;
    private String paymentStatus;
    private String bookChannel;

}
