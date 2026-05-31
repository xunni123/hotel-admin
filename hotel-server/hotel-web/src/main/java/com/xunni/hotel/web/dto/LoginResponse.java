package com.xunni.hotel.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private int code;
    private boolean success;
    private String message;
    private Long userId;
    private String username;
    private String token;
    private String role;
    private boolean isAdmin;
    private PermissionDTO permissions;
}
