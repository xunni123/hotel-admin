package com.xunni.hotel.web.config;

import org.springframework.context.annotation.Configuration;

/**
 * 密码策略配置类
 * 
 * 密码要求：
 * - 长度至少8位
 * - 包含至少一个大写字母
 * - 包含至少一个小写字母
 * - 包含至少一个数字
 * - 包含至少一个特殊字符
 */
@Configuration
public class PasswordPolicyConfig {

    private static final String PASSWORD_REGEX = 
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

    private static final String PASSWORD_ERROR_MSG = 
            "密码必须至少8位，包含大小写字母、数字和特殊字符(@$!%*?&)";

    /**
     * 验证密码强度
     */
    public static boolean validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }
        return password.matches(PASSWORD_REGEX);
    }

    /**
     * 获取密码错误提示信息
     */
    public static String getPasswordErrorMsg() {
        return PASSWORD_ERROR_MSG;
    }

    /**
     * 验证用户名
     */
    public static boolean validateUsername(String username) {
        if (username == null || username.isEmpty()) {
            return false;
        }
        // 用户名长度3-20，只能包含字母、数字、下划线
        return username.matches("^[a-zA-Z0-9_]{3,20}$");
    }

    /**
     * 获取用户名错误提示信息
     */
    public static String getUsernameErrorMsg() {
        return "用户名长度必须在3-20之间，只能包含字母、数字和下划线";
    }

    /**
     * 验证邮箱格式
     */
    public static boolean validateEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    /**
     * 验证手机号格式
     */
    public static boolean validatePhone(String phone) {
        if (phone == null || phone.isEmpty()) {
            return false;
        }
        return phone.matches("^1[3-9]\\d{9}$");
    }
}