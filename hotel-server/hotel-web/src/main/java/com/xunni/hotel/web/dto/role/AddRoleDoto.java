package com.xunni.hotel.web.dto.role;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AddRoleDoto {
    @NotNull(message = "角色ID不能为空")
    private Integer roleId;
    @NotBlank(message = "角色名称不能为空")
    private String roleName;
    private String roleKey;

    @NotBlank(message = "角色描述不能为空")
    private String description;


    private String status;
    private Integer sortOrder;
}
