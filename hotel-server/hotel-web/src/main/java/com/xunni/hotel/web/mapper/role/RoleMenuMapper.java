package com.xunni.hotel.web.mapper.role;

import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RoleMenuMapper {

    @Delete("DELETE FROM role_menus WHERE role_id = #{roleId}")
    int deleteByRoleId(Integer roleId);

    @Insert("<script>" +
            "INSERT INTO role_menus (role_id, menu_id) VALUES " +
            "<foreach collection='menuIds' item='menuId' separator=','>" +
            "(#{roleId}, #{menuId})" +
            "</foreach>" +
            "</script>")
    int insertBatch(@Param("roleId") Integer roleId, @Param("menuIds") List<Integer> menuIds);

    @Select("SELECT menu_id FROM role_menus WHERE role_id = #{roleId}")
    List<Integer> selectMenuIdsByRoleId(Integer roleId);

    @Select("SELECT COUNT(*) FROM role_menus WHERE role_id = #{roleId}")
    int countByRoleId(Integer roleId);
}
