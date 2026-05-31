package com.xunni.hotel.web.service.iml.role;

import com.xunni.hotel.web.dto.role.AddRoleDoto;
import com.xunni.hotel.web.mapper.role.AddRoleMapper;
import com.xunni.hotel.web.mapper.role.RoleMenuMapper;
import com.xunni.hotel.web.mapper.role.SelectRoleMapper;
import com.xunni.hotel.web.service.role.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private SelectRoleMapper selectRoleMapper;
    @Autowired
    private AddRoleMapper addRoleMapper;
    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Override
    public List<AddRoleDoto> selectRoleList(String roleName) {
        if (roleName == null || roleName.trim().isEmpty()) {
            return selectRoleMapper.selectRoleList();
        }
        return selectRoleMapper.selectRoleListByRoleName(roleName.trim());
    }

    @Override
    public List<AddRoleDoto> selectRoleListWithPage(String roleName, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        if (roleName == null || roleName.trim().isEmpty()) {
            return selectRoleMapper.selectRoleListWithPage(offset, pageSize);
        }
        return selectRoleMapper.selectRoleListByRoleNameWithPage(roleName.trim(), offset, pageSize);
    }

    @Override
    public int getRoleTotal(String roleName) {
        if (roleName == null || roleName.trim().isEmpty()) {
            return selectRoleMapper.getRoleTotal();
        }
        return selectRoleMapper.getRoleTotalByRoleName(roleName.trim());
    }

    // 查询角色列表
    @Override
    public List<AddRoleDoto> selectUserList() {
        return selectRoleMapper.selectRoleList();
    }

    // 更新角色
    @Override
    public int updateRole(AddRoleDoto role) {
        return addRoleMapper.update(role);
    }

    // 删除角色
    @Override
    public int deleteRole(Integer roleId) {
        return addRoleMapper.delete(roleId);
    }

    // 分配权限
    @Override
    @Transactional
    public int assignPermissions(Integer roleId, List<Integer> menuIds) {
        roleMenuMapper.deleteByRoleId(roleId);
        if (menuIds != null && !menuIds.isEmpty()) {
            return roleMenuMapper.insertBatch(roleId, menuIds);
        }
        return 0;
    }

    // 获取角色权限
    @Override
    public List<Integer> getRolePermissions(Integer roleId) {
        List<Integer> permissions = roleMenuMapper.selectMenuIdsByRoleId(roleId);
        return permissions != null ? permissions : new ArrayList<>();
    }

}