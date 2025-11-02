package com.iot.vehicle.web.integration;

import com.alibaba.druid.spring.boot3.autoconfigure.DruidDataSourceAutoConfigure;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iot.vehicle.api.dto.CreateDeviceDTO;
import com.iot.vehicle.api.dto.LoginDTO;
import com.iot.vehicle.api.dto.UpdateDeviceDTO;
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
 * 设备管理接口集成测试
 *
 * @author dongxiang.wu
 */
@SpringBootTest
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = {DruidDataSourceAutoConfigure.class})
@ActiveProfiles("dev")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DeviceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static String adminToken;
    private static Long testDeviceId;

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
    @DisplayName("1. 注册设备")
    void testRegisterDevice() throws Exception {
        CreateDeviceDTO deviceDTO = new CreateDeviceDTO();
        deviceDTO.setDeviceName("测试设备-自动化");
        deviceDTO.setDeviceSn("SN-AUTO-TEST-" + System.currentTimeMillis());
        deviceDTO.setDeviceType("VEHICLE_CAR");
        deviceDTO.setDeviceModel("Model-Test");
        deviceDTO.setManufacturer("测试厂商");
        deviceDTO.setProtocolType("MQTT");
        deviceDTO.setRemark("自动化测试设备");

        MvcResult result = mockMvc.perform(post("/device/register")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deviceDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.deviceId").exists())
                .andExpect(jsonPath("$.data.secretKey").exists())
                .andExpect(jsonPath("$.data.deviceName").value("测试设备-自动化"))
                .andReturn();

        // 保存设备ID供后续测试使用
        String responseBody = result.getResponse().getContentAsString();
        testDeviceId = objectMapper.readTree(responseBody)
                .get("data")
                .get("id")
                .asLong();

        System.out.println("注册的测试设备ID: " + testDeviceId);
    }

    @Test
    @Order(2)
    @DisplayName("2. 分页查询设备列表")
    void testGetDevicePage() throws Exception {
        mockMvc.perform(get("/device/page")
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
    @Order(3)
    @DisplayName("3. 按设备名称搜索")
    void testSearchDeviceByName() throws Exception {
        mockMvc.perform(get("/device/page")
                        .param("current", "1")
                        .param("size", "10")
                        .param("deviceName", "测试")
                        .header("Authorization", "Bearer " + adminToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.records").isArray());
    }

    @Test
    @Order(4)
    @DisplayName("4. 按设备类型筛选")
    void testFilterByDeviceType() throws Exception {
        mockMvc.perform(get("/device/page")
                        .param("current", "1")
                        .param("size", "10")
                        .param("deviceType", "VEHICLE_CAR")
                        .header("Authorization", "Bearer " + adminToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    @Order(5)
    @DisplayName("5. 查询设备详情")
    void testGetDeviceById() throws Exception {
        // 使用测试设备ID
        if (testDeviceId != null) {
            mockMvc.perform(get("/device/" + testDeviceId)
                            .header("Authorization", "Bearer " + adminToken))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.data.id").value(testDeviceId))
                    .andExpect(jsonPath("$.data.deviceName").value("测试设备-自动化"));
        }
    }

    @Test
    @Order(6)
    @DisplayName("6. 更新设备信息")
    void testUpdateDevice() throws Exception {
        if (testDeviceId != null) {
            UpdateDeviceDTO updateDTO = new UpdateDeviceDTO();
            updateDTO.setDeviceName("测试设备-已更新");
            updateDTO.setRemark("更新后的备注");

            mockMvc.perform(put("/device/" + testDeviceId)
                            .header("Authorization", "Bearer " + adminToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateDTO)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.message").value("操作成功"));
        }
    }

    @Test
    @Order(7)
    @DisplayName("7. 获取设备统计信息")
    void testGetStatistics() throws Exception {
        mockMvc.perform(get("/device/statistics")
                        .header("Authorization", "Bearer " + adminToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.totalCount").exists())
                .andExpect(jsonPath("$.data.onlineCount").exists())
                .andExpect(jsonPath("$.data.offlineCount").exists())
                .andExpect(jsonPath("$.data.onlineRate").exists())
                .andExpect(jsonPath("$.data.statusCount").exists());
    }

    @Test
    @Order(8)
    @DisplayName("8. 删除设备")
    void testDeleteDevice() throws Exception {
        if (testDeviceId != null) {
            mockMvc.perform(delete("/device/" + testDeviceId)
                            .header("Authorization", "Bearer " + adminToken))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.message").value("操作成功"));
        }
    }
}

