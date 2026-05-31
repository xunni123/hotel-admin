package com.xunni.hotel.entity;

import lombok.Data;

@Data
public class Booking {
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
