package com.xunni.hotel.web.controller.role;

import com.xunni.hotel.common.result.Result;
import com.xunni.hotel.web.dto.role.AddRoleDoto;
import com.xunni.hotel.web.service.role.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/role")
public class UpdateRoleController {

    @Autowired
    private RoleService roleService;
    
    @PostMapping("/update")
    public Result updateRole(@Validated @RequestBody AddRoleDoto addRoleDoto) {
        int result = roleService.updateRole(addRoleDoto);
        if (result > 0) {
            return Result.success("更新角色成功");
        } else {
            return Result.error("更新角色失败");
        }
    }
}
