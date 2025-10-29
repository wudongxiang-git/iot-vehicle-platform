package com.iot.vehicle.web.integration;

import com.alibaba.druid.spring.boot3.autoconfigure.DruidDataSourceAutoConfigure;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iot.vehicle.api.dto.ChangePasswordDTO;
import com.iot.vehicle.api.dto.LoginDTO;
import com.iot.vehicle.api.dto.UpdateUserDTO;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 用户管理接口集成测试
 * 
 * 测试用户CRUD、密码修改等功能
 *
 * @author dongxiang.wu
 */
@SpringBootTest
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = {DruidDataSourceAutoConfigure.class})
@ActiveProfiles("dev")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static String adminToken;

    @BeforeAll
    static void setup(@Autowired MockMvc mockMvc, @Autowired ObjectMapper objectMapper) throws Exception {
        // 登录获取Token
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("admin");
        loginDTO.setPassword("123456");

        MvcResult result = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        adminToken = objectMapper.readTree(responseBody)
                .get("data")
                .get("accessToken")
                .asText();

        System.out.println("=== 测试环境准备完成，Token已获取 ===");
    }

    @Test
    @Order(1)
    @DisplayName("1. 获取当前用户信息")
    void testGetCurrentUser() throws Exception {
        mockMvc.perform(get("/user/current")
                        .header("Authorization", "Bearer " + adminToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.username").value("admin"))
                .andExpect(jsonPath("$.data.id").value(1));
    }

    @Test
    @Order(2)
    @DisplayName("2. 未登录访问受保护接口应返回401")
    void testAccessWithoutToken() throws Exception {
        mockMvc.perform(get("/user/current"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(401))
                .andExpect(jsonPath("$.message").value("请先登录"));
    }

    @Test
    @Order(3)
    @DisplayName("3. 根据ID查询用户")
    void testGetUserById() throws Exception {
        mockMvc.perform(get("/user/1")
                        .header("Authorization", "Bearer " + adminToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.username").value("admin"));
    }

    @Test
    @Order(4)
    @DisplayName("4. 分页查询用户列表")
    void testGetUserPage() throws Exception {
        mockMvc.perform(get("/user/page")
                        .param("current", "1")
                        .param("size", "10")
                        .header("Authorization", "Bearer " + adminToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.records").isArray())
                .andExpect(jsonPath("$.data.total").exists())
                .andExpect(jsonPath("$.data.current").value(1))
                .andExpect(jsonPath("$.data.size").value(10));
    }

    @Test
    @Order(5)
    @DisplayName("5. 按用户名搜索用户")
    void testSearchUserByUsername() throws Exception {
        mockMvc.perform(get("/user/page")
                        .param("current", "1")
                        .param("size", "10")
                        .param("username", "admin")
                        .header("Authorization", "Bearer " + adminToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.records[0].username").value("admin"));
    }

    @Test
    @Order(6)
    @DisplayName("6. 更新用户信息")
    void testUpdateUser() throws Exception {
        UpdateUserDTO updateDTO = new UpdateUserDTO();
        updateDTO.setNickname("超级管理员（已更新）");
        updateDTO.setEmail("admin_updated@example.com");

        mockMvc.perform(put("/user/1")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("操作成功"));
    }

    @Test
    @Order(7)
    @DisplayName("7. 修改密码（改完再改回）")
    void testChangePassword() throws Exception {
        // 步骤1：修改密码 123456 → newpass123
        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO();
        changePasswordDTO.setOldPassword("123456");
        changePasswordDTO.setNewPassword("newpass123");

        mockMvc.perform(post("/user/change-password")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changePasswordDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("操作成功"));

        // 步骤2：用新密码登录验证
        LoginDTO loginDTONew = new LoginDTO();
        loginDTONew.setUsername("admin");
        loginDTONew.setPassword("newpass123");

        MvcResult loginResult = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTONew)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.accessToken").exists())
                .andReturn();

        // 获取新Token
        String responseBody = loginResult.getResponse().getContentAsString();
        String newToken = objectMapper.readTree(responseBody)
                .get("data")
                .get("accessToken")
                .asText();

        // 步骤3：改回原密码 newpass123 → 123456（保持数据库干净）
        ChangePasswordDTO changeBackDTO = new ChangePasswordDTO();
        changeBackDTO.setOldPassword("newpass123");
        changeBackDTO.setNewPassword("123456");

        mockMvc.perform(post("/user/change-password")
                        .header("Authorization", "Bearer " + newToken)  // 使用新Token
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changeBackDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        // 步骤4：用原密码登录验证已改回
        LoginDTO loginDTOOld = new LoginDTO();
        loginDTOOld.setUsername("admin");
        loginDTOOld.setPassword("123456");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTOOld)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    @Order(8)
    @DisplayName("8. 测试修改密码 - 旧密码错误")
    void testChangePasswordWithWrongOldPassword() throws Exception {
        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO();
        changePasswordDTO.setOldPassword("wrong-password");
        changePasswordDTO.setNewPassword("newpass123");

        mockMvc.perform(post("/user/change-password")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changePasswordDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("旧密码错误"));
    }

    @Test
    @Order(9)
    @DisplayName("9. 测试Token刷新")
    void testRefreshToken() throws Exception {
        mockMvc.perform(post("/auth/refresh")
                        .header("Authorization", "Bearer " + adminToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isString());
    }
}

