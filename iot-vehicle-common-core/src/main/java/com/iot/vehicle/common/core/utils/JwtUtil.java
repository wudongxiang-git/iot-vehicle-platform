package com.iot.vehicle.common.core.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 *
 * @author dongxiang.wu
 */
public class JwtUtil {

    /**
     * 默认密钥（生产环境应该从配置文件读取）
     */
    private static final String DEFAULT_SECRET = "iot-vehicle-platform-jwt-secret-key-2025-dongxiang-wu-project";

    /**
     * 默认过期时间（7天，单位：毫秒）
     */
    private static final long DEFAULT_EXPIRATION = 7 * 24 * 60 * 60 * 1000L;

    /**
     * 生成密钥
     */
    private static SecretKey getSecretKey(String secret) {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 生成JWT Token
     *
     * @param subject 主题（通常是用户ID或用户名）
     * @return Token字符串
     */
    public static String generateToken(String subject) {
        return generateToken(subject, new HashMap<>());
    }

    /**
     * 生成JWT Token（带额外声明）
     *
     * @param subject 主题（通常是用户ID或用户名）
     * @param claims  额外声明
     * @return Token字符串
     */
    public static String generateToken(String subject, Map<String, Object> claims) {
        return generateToken(subject, claims, DEFAULT_EXPIRATION);
    }

    /**
     * 生成JWT Token（自定义过期时间）
     *
     * @param subject    主题
     * @param claims     额外声明
     * @param expiration 过期时间（毫秒）
     * @return Token字符串
     */
    public static String generateToken(String subject, Map<String, Object> claims, long expiration) {
        return generateToken(subject, claims, expiration, DEFAULT_SECRET);
    }

    /**
     * 生成JWT Token（完全自定义）
     *
     * @param subject    主题
     * @param claims     额外声明
     * @param expiration 过期时间（毫秒）
     * @param secret     密钥
     * @return Token字符串
     */
    public static String generateToken(String subject, Map<String, Object> claims, long expiration, String secret) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .subject(subject)
                .claims(claims)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSecretKey(secret))
                .compact();
    }

    /**
     * 解析Token
     *
     * @param token Token字符串
     * @return Claims对象
     */
    public static Claims parseToken(String token) {
        return parseToken(token, DEFAULT_SECRET);
    }

    /**
     * 解析Token（自定义密钥）
     *
     * @param token  Token字符串
     * @param secret 密钥
     * @return Claims对象
     */
    public static Claims parseToken(String token, String secret) {
        try {
            return Jwts.parser()
                    .verifyWith(getSecretKey(secret))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            // 解析失败返回null
            return null;
        }
    }

    /**
     * 获取Token的主题（subject）
     *
     * @param token Token字符串
     * @return 主题
     */
    public static String getSubject(String token) {
        Claims claims = parseToken(token);
        return claims != null ? claims.getSubject() : null;
    }

    /**
     * 获取Token的声明
     *
     * @param token Token字符串
     * @param key   声明的key
     * @return 声明的值
     */
    public static Object getClaim(String token, String key) {
        Claims claims = parseToken(token);
        return claims != null ? claims.get(key) : null;
    }

    /**
     * 验证Token是否有效
     *
     * @param token Token字符串
     * @return true-有效，false-无效
     */
    public static boolean validateToken(String token) {
        return validateToken(token, DEFAULT_SECRET);
    }

    /**
     * 验证Token是否有效（自定义密钥）
     *
     * @param token  Token字符串
     * @param secret 密钥
     * @return true-有效，false-无效
     */
    public static boolean validateToken(String token, String secret) {
        try {
            Claims claims = parseToken(token, secret);
            if (claims == null) {
                return false;
            }
            
            // 检查是否过期
            Date expiration = claims.getExpiration();
            return expiration != null && expiration.after(new Date());
        } catch (Exception e) {
            // 验证失败返回false
            return false;
        }
    }

    /**
     * 检查Token是否过期
     *
     * @param token Token字符串
     * @return true-已过期，false-未过期
     */
    public static boolean isTokenExpired(String token) {
        try {
            Claims claims = parseToken(token);
            if (claims == null) {
                return true;
            }
            Date expiration = claims.getExpiration();
            return expiration != null && expiration.before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * 刷新Token
     *
     * @param token 旧Token
     * @return 新Token
     */
    public static String refreshToken(String token) {
        Claims claims = parseToken(token);
        if (claims == null) {
            return null;
        }
        
        String subject = claims.getSubject();
        Map<String, Object> claimsMap = new HashMap<>(claims);
        claimsMap.remove("iat"); // 移除签发时间
        claimsMap.remove("exp"); // 移除过期时间
        
        return generateToken(subject, claimsMap);
    }
}

