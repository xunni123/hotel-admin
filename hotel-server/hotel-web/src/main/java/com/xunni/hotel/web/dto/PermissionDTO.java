package com.xunni.hotel.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermissionDTO {
    private Boolean userManagement;
    private Boolean roleManagement;
    private Boolean menuManagement;
    private Boolean bookingManagement;
    private Boolean roomManagement;
    private Boolean orderManagement;
    private Boolean memberManagement;
    private Boolean reportManagement;
    private Boolean financialManagement;
    private Boolean canEdit;
    private Boolean canDelete;
    private Boolean canAssignPermission;
}
