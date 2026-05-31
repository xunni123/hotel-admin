package com.xunni.hotel.web.dto;

import lombok.Data;

@Data
public class CleanerWithTaskCountDTO {
    private Integer cleanerId;
    private String cleanerName;
    private String phone;
    private String avatar;
    private String status;
    private Long taskCount;
}
