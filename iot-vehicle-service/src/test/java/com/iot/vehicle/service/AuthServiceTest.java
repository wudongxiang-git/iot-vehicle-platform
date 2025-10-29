package com.iot.vehicle.service;

import com.iot.vehicle.api.dto.LoginDTO;
import com.iot.vehicle.api.dto.RegisterDTO;
import com.iot.vehicle.api.vo.LoginVO;
import com.iot.vehicle.common.core.exception.BusinessException;
import com.iot.vehicle.service.service.AuthService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 认证服务测试
 *
 * @author dongxiang.wu
 */
@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Test
    @Order(1)
    @DisplayName("管理员登录测试（admin/123456）")
    void testAdminLogin() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("admin");
        loginDTO.setPassword("123456");

        LoginVO loginVO = authService.login(loginDTO);

        assertNotNull(loginVO);
        assertNotNull(loginVO.getAccessToken());
        assertEquals("Bearer", loginVO.getTokenType());
        assertTrue(loginVO.getExpiresIn() > 0);
        assertNotNull(loginVO.getUserInfo());
        assertEquals("admin", loginVO.getUserInfo().getUsername());
        assertEquals(1L, loginVO.getUserInfo().getId());
    }

    @Test
    @Order(2)
    @DisplayName("注册新用户测试")
    void testRegister() {
        RegisterDTO registerDTO = new RegisterDTO();
        // 使用时间戳生成唯一用户名（只包含字母和数字）
        registerDTO.setUsername("test" + System.currentTimeMillis());
        registerDTO.setPassword("123456");
        registerDTO.setNickname("Service测试用户");
        registerDTO.setEmail("servicetest@example.com");
        registerDTO.setPhone("13900000001");

        assertDoesNotThrow(() -> authService.register(registerDTO));
    }

    @Test
    @Order(3)
    @DisplayName("用户名重复应抛出异常")
    void testRegisterDuplicateUsername() {
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setUsername("admin");
        registerDTO.setPassword("123456");

        BusinessException exception = assertThrows(BusinessException.class, 
                () -> authService.register(registerDTO));
        
        assertEquals("用户名已存在", exception.getMessage());
    }

    @Test
    @Order(4)
    @DisplayName("密码错误应抛出异常")
    void testLoginWithWrongPassword() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("admin");
        loginDTO.setPassword("wrong-password");

        BusinessException exception = assertThrows(BusinessException.class, 
                () -> authService.login(loginDTO));
        
        assertEquals("用户名或密码错误", exception.getMessage());
    }

    @Test
    @Order(5)
    @DisplayName("用户不存在应抛出异常")
    void testLoginWithNonExistentUser() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("nonexistent-user");
        loginDTO.setPassword("123456");

        BusinessException exception = assertThrows(BusinessException.class, 
                () -> authService.login(loginDTO));
        
        assertEquals("用户名或密码错误", exception.getMessage());
    }
}

