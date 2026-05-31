package com.xunni.hotel.web.mapper.role;

import com.xunni.hotel.web.dto.MenuTreeDoto;
import com.xunni.hotel.web.dto.role.AddRoleDoto;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SelectRoleMapper {
    @Select("SELECT role_id as roleId, role_name as roleName, role_key as roleKey, description, status, sort_order as sortOrder FROM roles")
    List<AddRoleDoto> selectRoleList();

    @Select("SELECT role_id as roleId, role_name as roleName, role_key as roleKey, description, status, sort_order as sortOrder FROM roles WHERE role_name LIKE CONCAT('%', #{roleName}, '%')")
    List<AddRoleDoto> selectRoleListByRoleName(String roleName);

    @Select("SELECT role_id as roleId, role_name as roleName, role_key as roleKey, description, status, sort_order as sortOrder FROM roles LIMIT #{offset}, #{pageSize}")
    List<AddRoleDoto> selectRoleListWithPage(@Param("offset") int offset, @Param("pageSize") int pageSize);

    @Select("SELECT role_id as roleId, role_name as roleName, role_key as roleKey, description, status, sort_order as sortOrder FROM roles WHERE role_name LIKE CONCAT('%', #{roleName}, '%') LIMIT #{offset}, #{pageSize}")
    List<AddRoleDoto> selectRoleListByRoleNameWithPage(@Param("roleName") String roleName, @Param("offset") int offset,
            @Param("pageSize") int pageSize);

    @Select("SELECT COUNT(*) FROM roles")
    int getRoleTotal();

    @Select("SELECT COUNT(*) FROM roles WHERE role_name LIKE CONCAT('%', #{roleName}, '%')")
    int getRoleTotalByRoleName(@Param("roleName") String roleName);

    @Select("SELECT role_id as roleId, role_name as roleName, role_key as roleKey, description, status, sort_order as sortOrder FROM roles WHERE role_id = #{roleId}")
    AddRoleDoto selectRoleById(@Param("roleId") Integer roleId);

    @Select("SELECT r.role_id as roleId, r.role_name as roleName, r.role_key as roleKey, r.description, r.status, r.sort_order as sortOrder "
            +
            "FROM roles r " +
            "JOIN user_roles ur ON r.role_id = ur.role_id " +
            "JOIN users u ON ur.user_id = u.user_id " +
            "WHERE u.username = #{username}")
    AddRoleDoto selectRoleByUsername(@Param("username") String username);
}
