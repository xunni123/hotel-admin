package com.xunni.hotel.web.service.iml;

import com.xunni.hotel.web.dto.PermissionDTO;
import com.xunni.hotel.web.mapper.MenuMapper;
import com.xunni.hotel.web.mapper.role.RoleMenuMapper;
import com.xunni.hotel.web.mapper.role.SelectRoleMapper;
import com.xunni.hotel.web.dto.role.AddRoleDoto;
import com.xunni.hotel.entity.Menu;
import com.xunni.hotel.web.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private SelectRoleMapper selectRoleMapper;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public PermissionDTO getPermissionsByRole(String role) {
        if (role == null) {
            return getGuestPermissions();
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
            default -> getPermissionsFromDb(role);
        };
    }

    private PermissionDTO getPermissionsFromDb(String roleName) {
        List<AddRoleDoto> roles = selectRoleMapper.selectByRoleName(roleName);
        if (roles.isEmpty()) {
            return getGuestPermissions();
        }
        Integer roleId = roles.get(0).getRoleId();
        List<Integer> menuIds = roleMenuMapper.selectMenuIdsByRoleId(roleId);
        if (menuIds.isEmpty()) {
            return getGuestPermissions();
        }

        Set<Integer> menuIdSet = menuIds.stream().collect(Collectors.toSet());
        Set<String> assignedKeys = menuMapper.selectAll().stream()
                .filter(m -> menuIdSet.contains(m.getMenu_id().intValue()))
                .map(Menu::getMenu_key)
                .filter(k -> k != null)
                .collect(Collectors.toSet());

        return new PermissionDTO(
                assignedKeys.contains("user") || assignedKeys.contains("userManagement"),
                assignedKeys.contains("role") || assignedKeys.contains("roleManagement"),
                assignedKeys.contains("menu") || assignedKeys.contains("menuManagement"),
                assignedKeys.contains("booking") || assignedKeys.contains("bookingManagement"),
                assignedKeys.contains("room") || assignedKeys.contains("roomManagement"),
                assignedKeys.contains("order") || assignedKeys.contains("orderManagement"),
                assignedKeys.contains("member") || assignedKeys.contains("memberManagement"),
                assignedKeys.contains("report") || assignedKeys.contains("reportManagement"),
                assignedKeys.contains("finance") || assignedKeys.contains("financialManagement"),
                assignedKeys.contains("log") || assignedKeys.contains("operationLogManagement"),
                assignedKeys.contains("notice") || assignedKeys.contains("noticeManagement"),
                assignedKeys.contains("goods") || assignedKeys.contains("goodsManagement"),
                assignedKeys.contains("add") || !assignedKeys.isEmpty(),
                assignedKeys.contains("edit") || !assignedKeys.isEmpty(),
                assignedKeys.contains("delete"),
                assignedKeys.contains("permission") || assignedKeys.contains("assignPermission"));
    }

    private PermissionDTO getAdminPermissions() {
        return new PermissionDTO(
                true, true, true, true, true, true,
                true, true, true, true, true, true,
                true, true, true, true);
    }

    private PermissionDTO getManagerPermissions() {
        return new PermissionDTO(
                true, true, false, true, true, true,
                true, true, true, true, true, true,
                true, true, true, true);
    }

    private PermissionDTO getFrontdeskSupervisorPermissions() {
        return new PermissionDTO(
                false, false, false, true, true, true,
                true, true, false, false, true, true,
                true, true, false, false);
    }

    private PermissionDTO getFrontdeskPermissions() {
        return new PermissionDTO(
                false, false, false, true, true, true,
                true, false, false, false, false, true,
                true, true, false, false);
    }

    private PermissionDTO getHousekeepingSupervisorPermissions() {
        return new PermissionDTO(
                false, false, false, false, true, true,
                true, true, false, false, false, false,
                true, true, false, false);
    }

    private PermissionDTO getHousekeepingPermissions() {
        return new PermissionDTO(
                false, false, false, false, true, false,
                false, false, false, false, false, false,
                false, false, false, false);
    }

    private PermissionDTO getFinancePermissions() {
        return new PermissionDTO(
                false, false, false, true, false, true,
                false, true, true, true, false, false,
                true, true, false, false);
    }

    private PermissionDTO getReadonlyPermissions() {
        return new PermissionDTO(
                false, false, false, true, true, true,
                true, true, true, true, true, true,
                false, false, false, false);
    }

    private PermissionDTO getGuestPermissions() {
        return new PermissionDTO(
                false, false, false, false, false, false,
                false, false, false, false, false, false,
                false, false, false, false);
    }
}
