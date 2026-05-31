package com.xunni.hotel.web.service;

import com.xunni.hotel.entity.Menu;
import com.xunni.hotel.web.dto.MenuTreeDoto;

import java.util.List;

public interface MenuService {
    List<Menu> getMenuList();

    List<Menu> getMenuListByUserId(Long userId);

    List<Menu> getAllMenus();

    int addMenu(Menu menu);

    int updateMenu(Menu menu);

    int deleteMenu(Integer menuId);

    List<MenuTreeDoto> getMenuTree();

    List<MenuTreeDoto> getMenuTreeByRoleId(Integer roleId);

}
