package com.xunni.hotel.web.service;

import com.xunni.hotel.web.dto.PermissionDTO;

public interface PermissionService {
    PermissionDTO getPermissionsByRole(String role);
}
