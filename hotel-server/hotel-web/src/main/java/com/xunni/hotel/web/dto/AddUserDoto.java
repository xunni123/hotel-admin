
package com.xunni.hotel.web.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AddUserDoto {
    private Long userId;

    @NotNull(message = "用户名称不能为空")
    private String username;

    private String password;
    private String realName;
    private String phone;
    private String email;
    private String avatar;
    private String status;
    private Long roleId;

}
