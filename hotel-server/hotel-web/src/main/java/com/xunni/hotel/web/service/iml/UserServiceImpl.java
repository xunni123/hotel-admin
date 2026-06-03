package com.xunni.hotel.web.service.iml;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xunni.hotel.common.utils.JwtUtil;
import com.xunni.hotel.entity.User;
import com.xunni.hotel.web.dto.AddUserDoto;
import com.xunni.hotel.web.dto.LoginRequest;
import com.xunni.hotel.web.dto.LoginResponse;
import com.xunni.hotel.web.dto.PermissionDTO;
import com.xunni.hotel.web.dto.role.AddRoleDoto;
import com.xunni.hotel.web.mapper.UserMapper;
import com.xunni.hotel.web.mapper.role.SelectRoleMapper;
import com.xunni.hotel.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PermissionServiceImpl permissionService;

    @Autowired
    private SelectRoleMapper selectRoleMapper;

    @Override
    public LoginResponse login(LoginRequest request) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, request.getUsername());
        User user = this.getOne(wrapper);

        if (user == null || !request.getPassword().equals(user.getPassword())) {
            return new LoginResponse(500, false, "用户名或密码错误", null, null, null, null, false, null);
        }

        String role = "普通用户";
        boolean isAdmin = false;

        AddRoleDoto roleDto = selectRoleMapper.selectRoleByUsername(request.getUsername());
        if (roleDto != null) {
            role = roleDto.getRoleName();
            isAdmin = "admin".equalsIgnoreCase(role) || "管理员".equals(role);
        }

        PermissionDTO permissions = permissionService.getPermissionsByRole(role);

        String token = jwtUtil.generateToken(
                user.getUserId(),
                user.getUsername(),
                user.getUsername(),
                user.getRealName(),
                role);

        return new LoginResponse(200, true, "登录成功", user.getUserId(), user.getUsername(), token, role, isAdmin,
                permissions);
    }

    // 查询分页
    @Override
    public List<User> selectUserList(String username, int page, int pageSize) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (username != null && !username.trim().isEmpty()) {
            wrapper.like(User::getUsername, username.trim());
        }
        int offset = (page - 1) * pageSize;
        return this.list(wrapper.last("LIMIT " + offset + ", " + pageSize));
    }

    // 获取用户总数
    @Override
    public int getUserTotal(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (username != null && !username.trim().isEmpty()) {
            wrapper.like(User::getUsername, username.trim());
        }
        return (int) this.count(wrapper);
    }

    @Autowired
    private UserMapper userMapper;

    // 查询用户列表
    @Override
    public List<User> selectAll() {
        return this.list();
    }

    // 更新用户
    @Override
    public int updateUser(AddUserDoto user) {
        return userMapper.updateUser(user);
    }

    // 删除
    public int deleteUser(Integer userId) {
        return userMapper.deleteUser(userId);
    }

    // 更新密码
    @Override
    public int updatePassword(Integer userId, String password) {
        String encodedPassword = passwordEncoder.encode(password);
        return userMapper.updatePassword(userId, encodedPassword);
    }

    //新增用户
    @Override
    public int insertUser(AddUserDoto user) {
        // 密码加密
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        
        // 状态转换：enabled -> 1, disabled -> 0
        if ("enabled".equalsIgnoreCase(user.getStatus())) {
            user.setStatus("1");
        } else if ("disabled".equalsIgnoreCase(user.getStatus())) {
            user.setStatus("0");
        }
        
        return userMapper.insertUser(user);
    }
}