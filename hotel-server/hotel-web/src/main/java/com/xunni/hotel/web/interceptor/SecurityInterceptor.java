package com.xunni.hotel.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * 安全日志拦截器 - 记录所有请求日志
 */
@Component
@Slf4j
public class SecurityInterceptor implements HandlerInterceptor {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 生成请求ID
        String requestId = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        request.setAttribute("requestId", requestId);
        
        // 获取请求信息
        String ip = getClientIp(request);
        String method = request.getMethod();
        String url = request.getRequestURI();
        String queryString = request.getQueryString();
        String userAgent = request.getHeader("User-Agent");
        
        // 记录请求日志
        log.info("[SECURITY] Request - ID: {}, IP: {}, Method: {}, URL: {}, Query: {}, Time: {}",
                requestId, ip, method, url, queryString, LocalDateTime.now().format(FORMATTER));
        
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, 
                               Object handler, Exception ex) {
        String requestId = (String) request.getAttribute("requestId");
        int status = response.getStatus();
        
        if (ex != null) {
            log.error("[SECURITY] Request Failed - ID: {}, Status: {}, Error: {}",
                    requestId, status, ex.getMessage(), ex);
        } else {
            log.info("[SECURITY] Request Completed - ID: {}, Status: {}, Time: {}",
                    requestId, status, LocalDateTime.now().format(FORMATTER));
        }
    }

    /**
     * 获取客户端真实IP
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 如果是多个代理，取第一个IP
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}