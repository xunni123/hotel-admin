package com.xunni.hotel.web.service.role;

import com.xunni.hotel.web.dto.role.AddRoleDoto;

import java.util.List;

public interface RoleService {
    List<AddRoleDoto> selectRoleList(String roleName);

    List<AddRoleDoto> selectRoleListWithPage(String roleName, int page, int pageSize);

    int getRoleTotal(String roleName);

    List<AddRoleDoto> selectUserList();

    int updateRole(AddRoleDoto role);

    int deleteRole(Integer roleId);

    int assignPermissions(Integer roleId, List<Integer> menuIds);

    List<Integer> getRolePermissions(Integer roleId);

}
