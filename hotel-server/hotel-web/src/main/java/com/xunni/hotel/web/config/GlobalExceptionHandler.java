package com.xunni.hotel.web.config;

import com.xunni.hotel.common.result.Result;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.sql.SQLException;
import java.util.stream.Collectors;

/**
 * 全局异常处理器
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 处理所有未捕获异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<Result<?>> handleException(Exception e, HttpServletRequest request) {
        log.error("[SECURITY] 服务器内部错误 - IP: {}, URL: {}, Error: {}", 
                getClientIp(request), request.getRequestURI(), e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Result.error(500, "服务器内部错误"));
    }

    /**
     * 处理运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public ResponseEntity<Result<?>> handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        log.error("[SECURITY] 运行时错误 - IP: {}, URL: {}, Error: {}", 
                getClientIp(request), request.getRequestURI(), e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Result.error(500, "运行时错误：" + e.getMessage()));
    }

    /**
     * 处理参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<Result<?>> handleValidationException(MethodArgumentNotValidException e, HttpServletRequest request) {
        String errors = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        log.warn("[SECURITY] 参数校验失败 - IP: {}, URL: {}, Errors: {}", 
                getClientIp(request), request.getRequestURI(), errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Result.error(400, "参数校验失败：" + errors));
    }

    /**
     * 处理缺少参数异常
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public ResponseEntity<Result<?>> handleMissingParameterException(MissingServletRequestParameterException e, HttpServletRequest request) {
        log.warn("[SECURITY] 缺少必要参数 - IP: {}, URL: {}, Parameter: {}", 
                getClientIp(request), request.getRequestURI(), e.getParameterName());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Result.error(400, "缺少必要参数：" + e.getParameterName()));
    }

    /**
     * 处理参数类型错误异常
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseBody
    public ResponseEntity<Result<?>> handleTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        log.warn("[SECURITY] 参数类型错误 - IP: {}, URL: {}, Error: {}", 
                getClientIp(request), request.getRequestURI(), e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Result.error(400, "参数类型错误：" + e.getName()));
    }

    /**
     * 处理数据库访问异常
     */
    @ExceptionHandler(DataAccessException.class)
    @ResponseBody
    public ResponseEntity<Result<?>> handleDataAccessException(DataAccessException e, HttpServletRequest request) {
        log.error("[SECURITY] 数据库访问错误 - IP: {}, URL: {}, Error: {}", 
                getClientIp(request), request.getRequestURI(), e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Result.error(500, "数据库访问错误"));
    }

    /**
     * 处理SQL异常
     */
    @ExceptionHandler(SQLException.class)
    @ResponseBody
    public ResponseEntity<Result<?>> handleSQLException(SQLException e, HttpServletRequest request) {
        log.error("[SECURITY] SQL执行错误 - IP: {}, URL: {}, Error: {}", 
                getClientIp(request), request.getRequestURI(), e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Result.error(500, "数据库操作失败"));
    }

    /**
     * 处理非法参数异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public ResponseEntity<Result<?>> handleIllegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
        log.warn("[SECURITY] 非法参数 - IP: {}, URL: {}, Error: {}", 
                getClientIp(request), request.getRequestURI(), e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Result.error(400, e.getMessage()));
    }

    /**
     * 处理认证异常
     */
    @ExceptionHandler(AuthenticationException.class)
    @ResponseBody
    public ResponseEntity<Result<?>> handleAuthenticationException(AuthenticationException e, HttpServletRequest request) {
        log.warn("[SECURITY] 认证失败 - IP: {}, URL: {}, Error: {}", 
                getClientIp(request), request.getRequestURI(), e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Result.error(401, "认证失败：" + e.getMessage()));
    }

    /**
     * 处理凭证错误异常
     */
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseBody
    public ResponseEntity<Result<?>> handleBadCredentialsException(BadCredentialsException e, HttpServletRequest request) {
        log.warn("[SECURITY] 凭证错误 - IP: {}, URL: {}", 
                getClientIp(request), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Result.error(401, "用户名或密码错误"));
    }

    /**
     * 处理权限拒绝异常
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public ResponseEntity<Result<?>> handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request) {
        log.warn("[SECURITY] 权限拒绝 - IP: {}, URL: {}, Error: {}", 
                getClientIp(request), request.getRequestURI(), e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Result.error(403, "权限不足，无法访问"));
    }

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public ResponseEntity<Result<?>> handleBusinessException(BusinessException e) {
        log.warn("[SECURITY] 业务异常 - Code: {}, Message: {}", e.getCode(), e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Result.error(e.getCode(), e.getMessage()));
    }

    /**
     * 获取客户端IP
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

    /**
     * 业务异常类
     */
    public static class BusinessException extends RuntimeException {
        private final int code;

        public BusinessException(int code, String message) {
            super(message);
            this.code = code;
        }

        public BusinessException(String message) {
            super(message);
            this.code = 400;
        }

        public int getCode() {
            return code;
        }
    }
}