package com.xunni.hotel.web.mapper.role;


import com.xunni.hotel.entity.AddRole;
import com.xunni.hotel.web.dto.role.AddRoleDoto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;

import java.util.List;

@Mapper
public interface AddRoleMapper {
    @Insert("INSERT INTO roles(role_name, role_key, description, sort_order, status, create_time) " +
            "VALUES(#{roleName}, #{roleKey}, #{description}, #{sortOrder}, #{status}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "roleId")
    int insert(AddRole role);

    //检查角色存在
    @Select("SELECT COUNT(*) FROM roles WHERE role_name = #{roleName}")
    int countByRoleName(String roleName);

    //检查角色键存在
    @Select("SELECT COUNT(*) FROM roles WHERE role_key = #{roleKey}")
    int countByRoleKey(String roleKey);

    @Select("SELECT * FROM roles WHERE role_id = #{roleId}")
    AddRole selectById(Integer roleId);

    //查询
    @Select("SELECT * FROM roles WHERE role_name LIKE CONCAT('%', #{roleName}, '%')")
    List<AddRole> selectByRoleName(String roleName);


    //更新角色
    @Update("UPDATE roles SET role_name = #{roleName}, role_key = #{roleKey}, description = #{description}, sort_order = #{sortOrder}, status = #{status} WHERE role_id = #{roleId}")
    int update(AddRoleDoto role);
    
    //删除角色
    @Delete("DELETE FROM roles WHERE role_id = #{roleId}")
    int delete(Integer roleId);
}
