package com.iot.vehicle.web.integration;

import com.alibaba.druid.spring.boot3.autoconfigure.DruidDataSourceAutoConfigure;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iot.vehicle.api.dto.CreateRoleDTO;
import com.iot.vehicle.api.dto.LoginDTO;
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
 * 角色管理接口集成测试
 *
 * @author dongxiang.wu
 */
@SpringBootTest
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = {DruidDataSourceAutoConfigure.class})
@ActiveProfiles("dev")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RoleIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static String adminToken;
    private static Long testRoleId;

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

        System.out.println("=== 角色测试环境准备完成 ===");
    }

    @Test
    @Order(1)
    @DisplayName("1. 创建角色")
    void testCreateRole() throws Exception {
        CreateRoleDTO roleDTO = new CreateRoleDTO();
        // 角色编码只能包含大写字母和下划线，使用随机字母而非时间戳
        String randomCode = "ROLE_TEST_AUTO";
        roleDTO.setRoleCode(randomCode);
        roleDTO.setRoleName("测试角色");
        roleDTO.setDescription("自动化测试角色");
        roleDTO.setSortOrder(100);

        MvcResult result = mockMvc.perform(post("/role")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roleDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").exists())
                .andReturn();

        // 保存角色ID
        String responseBody = result.getResponse().getContentAsString();
        testRoleId = objectMapper.readTree(responseBody).get("data").asLong();
        System.out.println("创建的测试角色ID: " + testRoleId);
    }

    @Test
    @Order(2)
    @DisplayName("2. 查询所有角色")
    void testGetAllRoles() throws Exception {
        mockMvc.perform(get("/role/all")
                        .header("Authorization", "Bearer " + adminToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    @Order(3)
    @DisplayName("3. 分页查询角色")
    void testGetRolePage() throws Exception {
        mockMvc.perform(get("/role/page")
                        .param("current", "1")
                        .param("size", "10")
                        .header("Authorization", "Bearer " + adminToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.records").isArray())
                .andExpect(jsonPath("$.data.total").exists());
    }

    @Test
    @Order(4)
    @DisplayName("4. 查询角色详情")
    void testGetRoleById() throws Exception {
        if (testRoleId != null) {
            mockMvc.perform(get("/role/" + testRoleId)
                            .header("Authorization", "Bearer " + adminToken))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.data.id").value(testRoleId));
        }
    }

    @Test
    @Order(5)
    @DisplayName("5. 更新角色")
    void testUpdateRole() throws Exception {
        if (testRoleId != null) {
            CreateRoleDTO roleDTO = new CreateRoleDTO();
            // 更新时保持原编码，只修改名称和描述
            roleDTO.setRoleCode("ROLE_TEST_AUTO");  // 保持与创建时相同
            roleDTO.setRoleName("测试角色-已更新");
            roleDTO.setDescription("更新后的描述");
            roleDTO.setSortOrder(200);

            mockMvc.perform(put("/role/" + testRoleId)
                            .header("Authorization", "Bearer " + adminToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(roleDTO)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(200));
        }
    }

    @Test
    @Order(6)
    @DisplayName("6. 删除角色")
    void testDeleteRole() throws Exception {
        if (testRoleId != null) {
            mockMvc.perform(delete("/role/" + testRoleId)
                            .header("Authorization", "Bearer " + adminToken))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(200));
        }
    }
}

