package com.xunni.hotel.entity;

import lombok.Data;

@Data
public class Room {
    private String roomId;
    private String roomNumber;
    private String building;
    private String floor;
    private String roomType;
    private Float price;
    private Integer capacity;
    private String status;
    private String statusName;
    private String statusColor;
    private String guestName;

    private String checkInTime;
    private String checkOutTime;
    private Integer currentGuests;

    @Data
    public static class Building {
        private Object value;
        private String label;
    }

    @Data
    public static class RoomStatus {
        private String status;
        private String status_name;
    }
}
