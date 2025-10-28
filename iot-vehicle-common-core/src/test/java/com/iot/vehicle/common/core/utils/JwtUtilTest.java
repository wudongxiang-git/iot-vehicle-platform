package com.iot.vehicle.common.core.utils;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JWT工具类测试
 *
 * @author dongxiang.wu
 */
class JwtUtilTest {

    @Test
    void testGenerateAndParseToken() {
        // 生成Token
        String userId = "123";
        String token = JwtUtil.generateToken(userId);
        
        assertNotNull(token);
        assertTrue(token.length() > 0);

        // 解析Token
        Claims claims = JwtUtil.parseToken(token);
        assertNotNull(claims);
        assertEquals(userId, claims.getSubject());
    }

    @Test
    void testGenerateTokenWithClaims() {
        // 生成Token（带额外声明）
        String userId = "123";
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", "testuser");
        claims.put("email", "test@example.com");
        
        String token = JwtUtil.generateToken(userId, claims);
        assertNotNull(token);

        // 解析并验证
        Claims parsedClaims = JwtUtil.parseToken(token);
        assertNotNull(parsedClaims);
        assertEquals(userId, parsedClaims.getSubject());
        assertEquals("testuser", parsedClaims.get("username"));
        assertEquals("test@example.com", parsedClaims.get("email"));
    }

    @Test
    void testGetSubject() {
        String userId = "123";
        String token = JwtUtil.generateToken(userId);
        
        String subject = JwtUtil.getSubject(token);
        assertEquals(userId, subject);
    }

    @Test
    void testGetClaim() {
        String userId = "123";
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", "testuser");
        
        String token = JwtUtil.generateToken(userId, claims);
        
        Object username = JwtUtil.getClaim(token, "username");
        assertEquals("testuser", username);
    }

    @Test
    void testValidateToken() {
        String token = JwtUtil.generateToken("123");
        assertTrue(JwtUtil.validateToken(token));
        
        // 测试无效Token
        assertFalse(JwtUtil.validateToken("invalid-token"));
        assertFalse(JwtUtil.validateToken(""));
        assertFalse(JwtUtil.validateToken(null));
    }

    @Test
    void testIsTokenExpired() {
        // 生成一个短期Token（1秒）
        String token = JwtUtil.generateToken("123", new HashMap<>(), 1000);
        assertFalse(JwtUtil.isTokenExpired(token));
        
        // 等待过期
        try {
            Thread.sleep(1100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        assertTrue(JwtUtil.isTokenExpired(token));
    }

    @Test
    void testRefreshToken() {
        String oldToken = JwtUtil.generateToken("123");
        assertNotNull(oldToken);
        
        // 刷新Token
        String newToken = JwtUtil.refreshToken(oldToken);
        assertNotNull(newToken);
        assertNotEquals(oldToken, newToken);
        
        // 验证新Token有效
        assertTrue(JwtUtil.validateToken(newToken));
    }
}

