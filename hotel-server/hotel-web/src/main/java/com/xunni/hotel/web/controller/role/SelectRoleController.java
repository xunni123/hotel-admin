package com.xunni.hotel.web.controller.role;


import com.xunni.hotel.common.result.Result;
import com.xunni.hotel.web.dto.role.AddRoleDoto;
import com.xunni.hotel.web.service.role.RoleService;
import com.xunni.hotel.web.service.role.addRoleService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/role")
public class SelectRoleController {

    @Autowired
    private RoleService roleService;



//    查询列表
    @GetMapping("/list")
    public Result roleList(@RequestParam(required = false) String roleName,
                          @RequestParam(defaultValue = "1") int page,
                          @RequestParam(defaultValue = "10") int pageSize){
        List<AddRoleDoto> roleList = roleService.selectRoleListWithPage(roleName, page, pageSize);
        int total = roleService.getRoleTotal(roleName);
        return Result.success(roleList, total);
    }
}
