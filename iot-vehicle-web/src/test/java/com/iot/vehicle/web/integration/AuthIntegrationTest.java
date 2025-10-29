package com.iot.vehicle.web.integration;

import com.alibaba.druid.spring.boot3.autoconfigure.DruidDataSourceAutoConfigure;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iot.vehicle.api.dto.LoginDTO;
import com.iot.vehicle.api.dto.RegisterDTO;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 认证接口集成测试
 * 
 * 测试登录、注册、登出等功能
 *
 * @author dongxiang.wu
 */
@SpringBootTest
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = {DruidDataSourceAutoConfigure.class})
@ActiveProfiles("dev")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static String adminToken;

    @Test
    @Order(1)
    @DisplayName("1. 测试管理员登录（admin/123456）")
    void testAdminLogin() throws Exception {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("admin");
        loginDTO.setPassword("123456");

        MvcResult result = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.accessToken").exists())
                .andExpect(jsonPath("$.data.userInfo.username").value("admin"))
                .andReturn();

        // 提取Token供后续测试使用
        String responseBody = result.getResponse().getContentAsString();
        adminToken = objectMapper.readTree(responseBody)
                .get("data")
                .get("accessToken")
                .asText();

        System.out.println("管理员Token: " + adminToken);
    }

    @Test
    @Order(2)
    @DisplayName("2. 测试用户注册")
    void testRegister() throws Exception {
        RegisterDTO registerDTO = new RegisterDTO();
        
        // 使用时间戳避免重复（只包含字母和数字）
        long timestamp = System.currentTimeMillis();
        String randomUsername = "test" + timestamp;
        
        // 生成随机手机号：13 + 时间戳后9位
        String randomPhone = "13" + String.valueOf(timestamp).substring(String.valueOf(timestamp).length() - 9);
        
        registerDTO.setUsername(randomUsername);
        registerDTO.setPassword("123456");
        registerDTO.setNickname("自动化测试用户");
        registerDTO.setEmail(randomUsername + "@example.com");
        registerDTO.setPhone(randomPhone);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("操作成功"));
    }

    @Test
    @Order(3)
    @DisplayName("3. 测试用户名已存在")
    void testRegisterDuplicateUsername() throws Exception {
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setUsername("admin"); // 使用已存在的用户名
        registerDTO.setPassword("123456");
        registerDTO.setNickname("测试");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("用户名已存在"));
    }

    @Test
    @Order(4)
    @DisplayName("4. 测试错误的用户名或密码")
    void testLoginWithWrongPassword() throws Exception {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("admin");
        loginDTO.setPassword("wrong-password");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(401))
                .andExpect(jsonPath("$.message").value("用户名或密码错误"));
    }

    @Test
    @Order(5)
    @DisplayName("5. 测试参数校验 - 用户名为空")
    void testRegisterWithBlankUsername() throws Exception {
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setUsername(""); // 空用户名
        registerDTO.setPassword("123456");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(400));
    }

    @Test
    @Order(6)
    @DisplayName("6. 测试登出")
    void testLogout() throws Exception {
        // 先登录获取Token
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("admin");
        loginDTO.setPassword("123456");

        MvcResult loginResult = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = loginResult.getResponse().getContentAsString();
        String token = objectMapper.readTree(responseBody)
                .get("data")
                .get("accessToken")
                .asText();

        // 登出
        mockMvc.perform(post("/auth/logout")
                        .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("操作成功"));
    }
}

