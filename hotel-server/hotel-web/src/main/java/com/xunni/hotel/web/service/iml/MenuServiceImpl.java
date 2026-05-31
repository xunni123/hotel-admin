package com.xunni.hotel.web.service.iml;

import com.xunni.hotel.entity.Menu;
import com.xunni.hotel.web.dto.MenuTreeDoto;
import com.xunni.hotel.web.mapper.MenuMapper;
import com.xunni.hotel.web.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public List<Menu> getMenuList() {
        List<Menu> allMenus = menuMapper.selectList();
        return buildTree(allMenus, 0L);
    }

    @Override
    public List<Menu> getMenuListByUserId(Long userId) {
        List<Menu> userMenus = menuMapper.selectMenusByUserId(userId);
        return buildTree(userMenus, 0L);
    }

    @Override
    public List<Menu> getAllMenus() {
        return menuMapper.selectAll();
    }

    @Override
    public int addMenu(Menu menu) {
        return menuMapper.insertMenu(menu);
    }

    @Override
    public int updateMenu(Menu menu) {
        return menuMapper.updateMenu(menu);
    }

    @Override
    public int deleteMenu(Integer menuId) {
        return menuMapper.deleteMenu(menuId);
    }

    private List<Menu> buildTree(List<Menu> menus, Long parentId) {
        return menus.stream()
                .filter(menu -> {
                    Long menuParentId = menu.getParent_id();
                    return menuParentId != null && menuParentId.equals(parentId);
                })
                .peek(menu -> menu.setChildren(buildTree(menus, menu.getMenu_id())))
                .collect(Collectors.toList());
    }

    @Override
    public List<MenuTreeDoto> getMenuTree() {
        List<MenuTreeDoto> allMenus = menuMapper.selectAllMenus();
        return buildMenuTree(allMenus, 0, null);
    }

    @Override
    public List<MenuTreeDoto> getMenuTreeByRoleId(Integer roleId) {
        // 查询该角色已有的菜单ID
        List<Integer> roleMenuIds = menuMapper.selectMenuIdsByRoleId(roleId);
        Set<Integer> roleMenuIdSet = new HashSet<>(roleMenuIds);

        // 查询角色菜单树（包含父菜单和子菜单）
        List<MenuTreeDoto> menus = menuMapper.selectMenuTreeByRoleId(roleId);

        // 构建树形结构，并标记已有权限的菜单
        return buildMenuTreeWithPermission(menus, 0, roleMenuIdSet);
    }

    private List<MenuTreeDoto> buildMenuTree(List<MenuTreeDoto> menus, Integer parentId, Set<Integer> roleMenuIds) {
        List<MenuTreeDoto> children = new ArrayList<>();
        for (MenuTreeDoto menu : menus) {
            if (parentId.equals(menu.getParentId())) {
                List<MenuTreeDoto> subChildren = buildMenuTree(menus, menu.getId(), roleMenuIds);
                menu.setChildren(subChildren);
                if (roleMenuIds != null) {
                    menu.setHasPermission(roleMenuIds.contains(menu.getId()));
                }
                children.add(menu);
            }
        }
        return children;
    }

    private List<MenuTreeDoto> buildMenuTreeWithPermission(List<MenuTreeDoto> menus, Integer parentId,
            Set<Integer> roleMenuIds) {
        List<MenuTreeDoto> children = new ArrayList<>();
        for (MenuTreeDoto menu : menus) {
            if (parentId.equals(menu.getParentId())) {
                List<MenuTreeDoto> subChildren = buildMenuTreeWithPermission(menus, menu.getId(), roleMenuIds);
                menu.setChildren(subChildren);
                menu.setHasPermission(roleMenuIds.contains(menu.getId()));
                children.add(menu);
            }
        }
        return children;
    }
}
