package com.iot.vehicle.common.core.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 密码工具类测试
 *
 * @author dongxiang.wu
 */
class PasswordUtilTest {

    @Test
    void testEncode() {
        String rawPassword = "123456";
        String encodedPassword = PasswordUtil.encode(rawPassword);
        
        assertNotNull(encodedPassword);
        assertTrue(encodedPassword.length() > 0);
        assertNotEquals(rawPassword, encodedPassword);
        
        // BCrypt编码结果应该以$2a$开头
        assertTrue(encodedPassword.startsWith("$2a$"));
    }

    @Test
    void testMatches() {
        String rawPassword = "123456";
        String encodedPassword = PasswordUtil.encode(rawPassword);
        
        // 正确的密码应该匹配
        assertTrue(PasswordUtil.matches(rawPassword, encodedPassword));
        
        // 错误的密码不应该匹配
        assertFalse(PasswordUtil.matches("wrong-password", encodedPassword));
        assertFalse(PasswordUtil.matches("", encodedPassword));
    }

    @Test
    void testEncodeTwice() {
        // BCrypt每次编码结果都不同（因为盐值随机）
        String rawPassword = "123456";
        String encoded1 = PasswordUtil.encode(rawPassword);
        String encoded2 = PasswordUtil.encode(rawPassword);
        
        assertNotEquals(encoded1, encoded2);
        
        // 但都应该能匹配原始密码
        assertTrue(PasswordUtil.matches(rawPassword, encoded1));
        assertTrue(PasswordUtil.matches(rawPassword, encoded2));
    }
}

