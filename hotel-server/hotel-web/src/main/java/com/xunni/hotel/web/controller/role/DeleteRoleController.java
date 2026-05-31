package com.xunni.hotel.web.controller.role;

import com.xunni.hotel.common.result.Result;
import com.xunni.hotel.web.service.role.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/role")
public class DeleteRoleController {

    @Autowired
    private RoleService roleService;
    
    @DeleteMapping("/delete/{roleId}")
    public Result deleteRole(@PathVariable Integer roleId) {
        int result = roleService.deleteRole(roleId);
        if (result > 0) {
            return Result.success("删除角色成功");
        } else {
            return Result.error("删除角色失败");
        }
    }
}
