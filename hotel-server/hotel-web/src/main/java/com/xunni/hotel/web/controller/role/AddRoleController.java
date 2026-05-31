package com.xunni.hotel.web.controller.role;


import com.xunni.hotel.common.result.Result;
import com.xunni.hotel.web.dto.role.AddRoleDoto;
import com.xunni.hotel.web.service.role.addRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/role")
public class AddRoleController {

    @Autowired
    private addRoleService addRoleService;
    @PostMapping("/add")

   public Result addRole(@Validated @RequestBody AddRoleDoto dto){
        addRoleService.addRole(dto);
        return Result.success("添加成功");
    }
}
