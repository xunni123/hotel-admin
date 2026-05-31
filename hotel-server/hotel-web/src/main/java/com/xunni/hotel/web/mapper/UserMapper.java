package com.xunni.hotel.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xunni.hotel.entity.User;
import com.xunni.hotel.web.dto.AddUserDoto;
import org.apache.ibatis.annotations.*;

import java.util.List;
@Mapper
public interface UserMapper extends BaseMapper<User> {

    //查询 用户 -login
    @Select("select * from roles where username = #{username}")
    User selectByUsername(@Param("username") String username);

    //查询所有用户
    @Select("select * from users")
    User selectAll();

    //查询用户列表
    @Select("select * from users where username like #{username}")
    User selectUser(@Param("username") String username);

    //更新用户
    @Update("update users set username=#{username}, password=#{password}, real_name=#{realName}, phone=#{phone}, email=#{email}, avatar=#{avatar}, status=#{status} where user_id=#{userId}")
    int updateUser(AddUserDoto user);

    //删除用户
    @Delete("delete from users where user_id=#{userId}")
    int deleteUser(Integer userId);

    //更新用户密码
    @Update("update users set password=#{password} where user_id=#{userId}")
    int updatePassword(@Param("userId") Integer userId, @Param("password") String password);
}
