package com.xunni.hotel.entity.HomeCard;

import java.time.LocalDateTime;

public class Room {
    private Integer roomId;
    private String roomNumber;
    private Integer typeId;
    private Integer floor;
    private String status;          // 房间状态（clean/dirty/maintenance等）
    private String features;
    private Integer sortOrder;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
