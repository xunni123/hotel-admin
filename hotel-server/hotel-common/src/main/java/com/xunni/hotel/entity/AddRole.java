package com.xunni.hotel.entity;

import lombok.Data;

@Data
public class AddRole {
    private Integer roleId;
    private String roleName;
    private String roleKey;
    private String status;
    private String description;
    private Integer sortOrder;
}
