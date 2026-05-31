package com.xunni.hotel.web.controller.role;

import com.xunni.hotel.common.result.Result;
import com.xunni.hotel.web.service.role.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/role")
public class RolePermissionController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/{roleId}/permissions")
    public Result getRolePermissions(@PathVariable Integer roleId) {
        List<Integer> permissions = roleService.getRolePermissions(roleId);
        return Result.success(permissions);
    }

    @PostMapping("/{roleId}/permissions")
    public Result assignPermissions(@PathVariable Integer roleId, @RequestBody Map<String, List<Integer>> request) {
        List<Integer> menuIds = request.get("menuIds");
        int count = roleService.assignPermissions(roleId, menuIds);
        return Result.success("权限分配成功", count);
    }
}
