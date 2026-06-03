package com.xunni.hotel.web.controller;

import com.xunni.hotel.common.result.Result;
import com.xunni.hotel.entity.Menu;
import com.xunni.hotel.web.dto.MenuTreeDoto;
import com.xunni.hotel.web.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping("/tree")
    public Result<List<Menu>> getMenuTree() {
        List<Menu> menuList = menuService.getMenuList();
        return Result.success(menuList);
    }

    @GetMapping("/tree2")
    public Result<List<MenuTreeDoto>> getMenuTree2() {
        List<MenuTreeDoto> menuTree = menuService.getMenuTree();
        return Result.success(menuTree);
    }

    @GetMapping("/tree/{userId}")
    public Result<List<Menu>> getMenuTreeByUserId(@PathVariable Long userId) {
        List<Menu> menuList = menuService.getMenuListByUserId(userId);
        return Result.success(menuList);
    }

    @GetMapping("/role/{roleId}/tree")
    public Result<List<MenuTreeDoto>> getMenuTreeByRoleId(@PathVariable Integer roleId) {
        List<MenuTreeDoto> menuTree = menuService.getMenuTreeByRoleId(roleId);
        return Result.success(menuTree);
    }

    @GetMapping("/list")
    public Result<List<Menu>> getMenuList() {
        // 返回所有菜单的扁平化列表（包含所有层级的菜单）
        List<Menu> menuList = menuService.getAllMenus();
        return Result.success(menuList);
    }

    @GetMapping("/list/tree")
    public Result<List<Menu>> getMenuListTree() {
        // 返回树形结构的菜单（仅顶级菜单，包含子菜单）
        List<Menu> menuList = menuService.getMenuList();
        return Result.success(menuList);
    }

    @GetMapping("/all")
    public Result<List<Menu>> getAllMenus() {
        List<Menu> menuList = menuService.getAllMenus();
        return Result.success(menuList);
    }

    @PostMapping("/add")
    public Result addMenu(@RequestBody Menu menu) {
        int result = menuService.addMenu(menu);
        if (result > 0) {
            return Result.success("添加菜单成功");
        }
        return Result.error("添加菜单失败");
    }

    @PostMapping("/update")
    public Result updateMenu(@RequestBody Menu menu) {
        int result = menuService.updateMenu(menu);
        if (result > 0) {
            return Result.success("更新菜单成功");
        }
        return Result.error("更新菜单失败");
    }

    @DeleteMapping("/delete/{menuId}")
    public Result deleteMenu(@PathVariable Integer menuId) {
        int result = menuService.deleteMenu(menuId);
        if (result > 0) {
            return Result.success("删除菜单成功");
        }
        return Result.error("删除菜单失败");
    }

}
