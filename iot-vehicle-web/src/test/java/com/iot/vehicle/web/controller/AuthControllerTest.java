package com.iot.vehicle.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iot.vehicle.api.dto.LoginDTO;
import com.iot.vehicle.api.vo.LoginVO;
import com.iot.vehicle.api.vo.UserVO;
import com.iot.vehicle.service.service.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * AuthController 单元测试
 * 
 * 使用@WebMvcTest只加载Controller层，不需要完整的Spring上下文
 * 使用MockBean模拟Service层
 *
 * @author dongxiang.wu
 */
@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @Test
    @DisplayName("测试用户登录接口")
    void testLogin() throws Exception {
        // Mock数据
        LoginVO loginVO = new LoginVO();
        loginVO.setAccessToken("mock-token-123456");
        loginVO.setTokenType("Bearer");
        loginVO.setExpiresIn(604800L);
        
        UserVO userVO = new UserVO();
        userVO.setId(1L);
        userVO.setUsername("admin");
        userVO.setNickname("管理员");
        loginVO.setUserInfo(userVO);

        // Mock Service行为
        when(authService.login(any(LoginDTO.class))).thenReturn(loginVO);

        // 构造请求
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("admin");
        loginDTO.setPassword("123456");

        // 执行测试
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.accessToken").value("mock-token-123456"))
                .andExpect(jsonPath("$.data.userInfo.username").value("admin"));
    }

    @Test
    @DisplayName("测试登录参数校验")
    void testLoginValidation() throws Exception {
        // 用户名为空
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("");
        loginDTO.setPassword("123456");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(400));
    }
}

