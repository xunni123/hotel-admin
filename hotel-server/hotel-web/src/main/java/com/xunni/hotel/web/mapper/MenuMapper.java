package com.xunni.hotel.web.mapper;

import com.xunni.hotel.entity.Menu;
import com.xunni.hotel.web.dto.MenuTreeDoto;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;

import java.util.List;

public interface MenuMapper {

        @Select("SELECT * FROM menus WHERE status=1 ORDER BY sort_order")
        List<Menu> selectList();

        @Select("SELECT * FROM menus ORDER BY sort_order")
        List<Menu> selectAll();

        @Select("SELECT m.* FROM menus m " +
                        "INNER JOIN role_menus rm ON m.menu_id=rm.menu_id " +
                        "INNER JOIN user_roles ur ON rm.role_id=ur.role_id " +
                        "WHERE ur.user_id=#{userId} AND m.status=1 " +
                        "ORDER BY m.sort_order")
        List<Menu> selectMenusByUserId(@Param("userId") Long userId);

        @Insert("INSERT INTO menus (parent_id, menu_name, menu_key, menu_type, path, component, icon, sort_order, status) "
                        +
                        "VALUES (#{parent_id}, #{menu_name}, #{menu_key}, #{menu_type}, #{path}, #{component}, #{icon}, #{sort_order}, #{status})")
        @Options(useGeneratedKeys = true, keyProperty = "menu_id")
        int insertMenu(Menu menu);

        @Update("UPDATE menus SET parent_id=#{parent_id}, menu_name=#{menu_name}, menu_key=#{menu_key}, " +
                        "menu_type=#{menu_type}, path=#{path}, component=#{component}, icon=#{icon}, " +
                        "sort_order=#{sort_order}, status=#{status} WHERE menu_id=#{menu_id}")
        int updateMenu(Menu menu);

        @Delete("DELETE FROM menus WHERE menu_id=#{menuId}")
        int deleteMenu(@Param("menuId") Integer menuId);

        // 递归列表
        @Select("SELECT menu_id AS id, parent_id AS parentId, menu_name AS name, " +
                        "path, component, icon, sort_order AS `order`, status, menu_type AS menuType, menu_key AS menuKey "
                        +
                        "FROM menus WHERE status = 1 ORDER BY parent_id, sort_order")
        List<MenuTreeDoto> selectAllMenus();

        // 查询角色已有的菜单ID
        @Select("SELECT menu_id FROM role_menus WHERE role_id = #{roleId}")
        List<Integer> selectMenuIdsByRoleId(@Param("roleId") Integer roleId);

        // 查询角色菜单树（包含父菜单和子菜单）
        @Select("SELECT " +
                        "menu_id AS id, parent_id AS parentId, menu_name AS name, " +
                        "path, component, icon, sort_order AS `order`, status, menu_type AS menuType, menu_key AS menuKey "
                        +
                        "FROM menus " +
                        "WHERE status = 1 " +
                        "AND (menu_id IN (SELECT menu_id FROM role_menus WHERE role_id = #{roleId}) " +
                        "OR parent_id IN (" +
                        "    SELECT DISTINCT menu_id FROM role_menus WHERE role_id = #{roleId}" +
                        ")) " +
                        "ORDER BY parent_id, sort_order")
        List<MenuTreeDoto> selectMenuTreeByRoleId(@Param("roleId") Integer roleId);
}
