package com.xunni.hotel.web.aspect;

import com.xunni.hotel.common.result.Result;
import com.xunni.hotel.web.annotation.RateLimit;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * 限流切面
 * 
 * 只在 Redis 可用时才启用
 */
@Aspect
@Component
@Slf4j
@ConditionalOnBean(RedisTemplate.class)
public class RateLimitAspect {

    private final RedisTemplate<String, Long> redisTemplate;

    public RateLimitAspect(RedisTemplate<String, Long> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Around("@annotation(rateLimit)")
    public Object around(ProceedingJoinPoint joinPoint, RateLimit rateLimit) throws Throwable {
        int window = rateLimit.window();
        int maxCount = rateLimit.maxCount();
        RateLimit.LimitType type = rateLimit.type();
        
        String key = generateKey(joinPoint, type);
        
        // 获取当前请求数
        Long count = redisTemplate.opsForValue().get(key);
        if (count == null) {
            // 首次请求，设置初始值
            redisTemplate.opsForValue().set(key, 1L, window, TimeUnit.SECONDS);
        } else if (count >= maxCount) {
            // 超过限流阈值
            log.warn("[RATE_LIMIT] Request blocked - Key: {}, Window: {}s, Max: {}, Current: {}",
                    key, window, maxCount, count);
            return Result.error(429, "请求过于频繁，请稍后重试");
        } else {
            // 增加计数
            redisTemplate.opsForValue().increment(key);
        }
        
        return joinPoint.proceed();
    }

    /**
     * 生成限流Key
     */
    private String generateKey(ProceedingJoinPoint joinPoint, RateLimit.LimitType type) {
        StringBuilder key = new StringBuilder("rate_limit:");
        
        switch (type) {
            case IP:
                key.append("ip:").append(getClientIp());
                break;
            case USER:
                // 这里可以从JWT Token中获取用户ID
                key.append("user:").append(getCurrentUserId());
                break;
            case API:
                MethodSignature signature = (MethodSignature) joinPoint.getSignature();
                Method method = signature.getMethod();
                key.append("api:").append(method.getDeclaringClass().getName()).append(":").append(method.getName());
                break;
        }
        
        return key.toString();
    }

    /**
     * 获取客户端IP
     */
    private String getClientIp() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return "unknown";
        }
        
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
     * 获取当前用户ID（从Token中解析）
     */
    private String getCurrentUserId() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return "anonymous";
        }
        
        // 这里应该从JWT Token中解析用户ID
        // 示例：从Authorization头获取Token并解析
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            // 实际项目中这里需要调用JWT工具类解析
            return "user_" + System.currentTimeMillis();
        }
        
        return "anonymous";
    }

    private HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes != null ? attributes.getRequest() : null;
    }
}