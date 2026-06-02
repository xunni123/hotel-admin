package com.xunni.hotel.web.service.iml;

import com.xunni.hotel.web.dto.PermissionDTO;
import com.xunni.hotel.web.service.PermissionService;
import org.springframework.stereotype.Service;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Override
    public PermissionDTO getPermissionsByRole(String role) {
        if (role == null) {
            return getDefaultPermissions();
        }

        return switch (role.toLowerCase()) {
            case "admin" -> getAdminPermissions();
            case "manager", "酒店经理" -> getManagerPermissions();
            case "frontdesk_supervisor", "前台主管" -> getFrontdeskSupervisorPermissions();
            case "frontdesk", "前台员工" -> getFrontdeskPermissions();
            case "housekeeping_supervisor", "客房主管" -> getHousekeepingSupervisorPermissions();
            case "housekeeping", "客房服务员" -> getHousekeepingPermissions();
            case "finance", "财务人员" -> getFinancePermissions();
            case "readonly", "只读用户" -> getReadonlyPermissions();
            case "guest", "xunni", "新员工" -> getGuestPermissions();
            default -> getDefaultPermissions();
        };
    }

    private PermissionDTO getAdminPermissions() {
        return new PermissionDTO(
                true, // userManagement
                true, // roleManagement
                true, // menuManagement
                true, // bookingManagement
                true, // roomManagement
                true, // orderManagement
                true, // memberManagement
                true, // reportManagement
                true, // financialManagement
                true, // operationLogManagement
                true, // noticeManagement
                true, // goodsManagement
                true, // canAdd
                true, // canEdit
                true, // canDelete
                true // canAssignPermission
        );
    }

    private PermissionDTO getManagerPermissions() {
        return new PermissionDTO(
                true, // userManagement
                true, // roleManagement
                false, // menuManagement
                true, // bookingManagement
                true, // roomManagement
                true, // orderManagement
                true, // memberManagement
                true, // reportManagement
                true, // financialManagement
                true, // operationLogManagement
                true, // noticeManagement
                true, // goodsManagement
                true, // canAdd
                true, // canEdit
                true, // canDelete
                true // canAssignPermission
        );
    }

    private PermissionDTO getFrontdeskSupervisorPermissions() {
        return new PermissionDTO(
                false, // userManagement
                false, // roleManagement
                false, // menuManagement
                true, // bookingManagement
                true, // roomManagement
                true, // orderManagement
                true, // memberManagement
                true, // reportManagement
                false, // financialManagement
                false, // operationLogManagement
                true, // noticeManagement
                true, // goodsManagement
                true, // canAdd
                true, // canEdit
                false, // canDelete
                false // canAssignPermission
        );
    }

    private PermissionDTO getFrontdeskPermissions() {
        return new PermissionDTO(
                false, // userManagement
                false, // roleManagement
                false, // menuManagement
                true, // bookingManagement
                true, // roomManagement
                true, // orderManagement
                true, // memberManagement
                false, // reportManagement
                false, // financialManagement
                false, // operationLogManagement
                false, // noticeManagement
                true, // goodsManagement
                true, // canAdd
                true, // canEdit
                false, // canDelete
                false // canAssignPermission
        );
    }

    private PermissionDTO getHousekeepingSupervisorPermissions() {
        return new PermissionDTO(
                false, // userManagement
                false, // roleManagement
                false, // menuManagement
                false, // bookingManagement
                true, // roomManagement
                true, // orderManagement
                true, // memberManagement
                true, // reportManagement
                false, // financialManagement
                false, // operationLogManagement
                false, // noticeManagement
                false, // goodsManagement
                true, // canAdd
                true, // canEdit
                false, // canDelete
                false // canAssignPermission
        );
    }

    private PermissionDTO getHousekeepingPermissions() {
        return new PermissionDTO(
                false, // userManagement
                false, // roleManagement
                false, // menuManagement
                false, // bookingManagement
                true, // roomManagement
                false, // orderManagement
                false, // memberManagement
                false, // reportManagement
                false, // financialManagement
                false, // operationLogManagement
                false, // noticeManagement
                false, // goodsManagement
                false, // canAdd
                false, // canEdit
                false, // canDelete
                false // canAssignPermission
        );
    }

    private PermissionDTO getFinancePermissions() {
        return new PermissionDTO(
                false, // userManagement
                false, // roleManagement
                false, // menuManagement
                true, // bookingManagement
                false, // roomManagement
                true, // orderManagement
                false, // memberManagement
                true, // reportManagement
                true, // financialManagement
                true, // operationLogManagement
                false, // noticeManagement
                false, // goodsManagement
                true, // canAdd
                true, // canEdit
                false, // canDelete
                false // canAssignPermission
        );
    }

    private PermissionDTO getReadonlyPermissions() {
        return new PermissionDTO(
                false, // userManagement
                false, // roleManagement
                false, // menuManagement
                true, // bookingManagement
                true, // roomManagement
                true, // orderManagement
                true, // memberManagement
                true, // reportManagement
                true, // financialManagement
                true, // operationLogManagement
                true, // noticeManagement
                true, // goodsManagement
                false, // canAdd
                false, // canEdit
                false, // canDelete
                false // canAssignPermission
        );
    }

    private PermissionDTO getGuestPermissions() {
        return new PermissionDTO(
                false, // userManagement
                false, // roleManagement
                false, // menuManagement
                false, // bookingManagement
                false, // roomManagement
                false, // orderManagement
                false, // memberManagement
                false, // reportManagement
                false, // financialManagement
                false, // operationLogManagement
                false, // noticeManagement
                false, // goodsManagement
                false, // canAdd
                false, // canEdit
                false, // canDelete
                false // canAssignPermission
        );
    }

    private PermissionDTO getDefaultPermissions() {
        return new PermissionDTO(
                false, // userManagement
                false, // roleManagement
                false, // menuManagement
                false, // bookingManagement
                false, // roomManagement
                false, // orderManagement
                false, // memberManagement
                false, // reportManagement
                false, // financialManagement
                false, // operationLogManagement
                false, // noticeManagement
                false, // goodsManagement
                false, // canAdd
                false, // canEdit
                false, // canDelete
                false // canAssignPermission
        );
    }
}
