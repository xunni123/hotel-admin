package com.xunni.hotel.web.controller;

import com.xunni.hotel.common.result.Result;
import com.xunni.hotel.entity.User;
import com.xunni.hotel.web.dto.AddUserDoto;
import com.xunni.hotel.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public Result userList(@RequestParam(required = false) String username,
                           @RequestParam(defaultValue = "1") int page,
                           @RequestParam(defaultValue = "10") int pageSize) {
        List<User> userList = userService.selectUserList(username, page, pageSize);
        int total = userService.getUserTotal(username);
        return Result.success(userList, total);
    }

    @GetMapping("/all")
    public Result userAll() {
        List<User> userList = userService.selectAll();
        return Result.success(userList);
    }


    //更新用户
    @PostMapping("/update")
    public  Result updateUser(@Validated @RequestBody AddUserDoto userDoto){
        int result = userService.updateUser(userDoto);
        if (result > 0) {
            return Result.success("更新用户成功");
        } else {
            return Result.error("更新用户失败");
        }
    }

    //
    @DeleteMapping("/delete/{userId}")
    public  Result deleteUser(@PathVariable("userId") Integer userId){
        int result = userService.deleteUser(userId);
        if (result > 0) {
            return Result.success("删除用户成功");
        } else {
            return Result.error("删除用户失败");
        }
    }

    // 更新用户密码
    @PutMapping("/updatePassword/{userId}")
    public Result updatePassword(@PathVariable Integer userId, @RequestBody String password) {
        if (userId == null || password == null || password.trim().isEmpty()) {
            return Result.error("参数错误");
        }
        int result = userService.updatePassword(userId, password.trim());
        if (result > 0) {
            return Result.success("密码更新成功");
        } else {
            return Result.error("密码更新失败");
        }
    }
}