package com.xunni.hotel.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xunni.hotel.entity.User;
import com.xunni.hotel.web.dto.AddUserDoto;
import com.xunni.hotel.web.dto.LoginRequest;
import com.xunni.hotel.web.dto.LoginResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
public interface UserService extends IService<User> {
    LoginResponse login(LoginRequest request);
    List<User> selectUserList(String username, int page, int pageSize);
    int getUserTotal(String username);

    List< User> selectAll();

    int updateUser(AddUserDoto user);

    int deleteUser(Integer userId);

    int updatePassword(Integer userId, String password);

    int insertUser(AddUserDoto user);

}
