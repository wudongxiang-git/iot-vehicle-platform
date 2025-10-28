package com.iot.vehicle.common.core.result;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Result测试类
 *
 * @author dongxiang.wu
 */
class ResultTest {

    @Test
    void testSuccess() {
        Result<String> result = Result.success();
        
        assertEquals(200, result.getCode());
        assertEquals("操作成功", result.getMessage());
        assertNull(result.getData());
        assertNotNull(result.getTimestamp());
        assertTrue(result.isSuccess());
    }

    @Test
    void testSuccessWithData() {
        String data = "test data";
        Result<String> result = Result.success(data);
        
        assertEquals(200, result.getCode());
        assertEquals("操作成功", result.getMessage());
        assertEquals(data, result.getData());
        assertTrue(result.isSuccess());
    }

    @Test
    void testSuccessWithMessageAndData() {
        String message = "custom success message";
        String data = "test data";
        Result<String> result = Result.success(message, data);
        
        assertEquals(200, result.getCode());
        assertEquals(message, result.getMessage());
        assertEquals(data, result.getData());
        assertTrue(result.isSuccess());
    }

    @Test
    void testFail() {
        Result<Void> result = Result.fail();
        
        assertEquals(400, result.getCode());
        assertEquals("操作失败", result.getMessage());
        assertFalse(result.isSuccess());
    }

    @Test
    void testFailWithMessage() {
        String message = "custom error message";
        Result<Void> result = Result.fail(message);
        
        assertEquals(400, result.getCode());
        assertEquals(message, result.getMessage());
        assertFalse(result.isSuccess());
    }

    @Test
    void testFailWithCodeAndMessage() {
        Integer code = 500;
        String message = "internal error";
        Result<Void> result = Result.fail(code, message);
        
        assertEquals(code, result.getCode());
        assertEquals(message, result.getMessage());
        assertFalse(result.isSuccess());
    }

    @Test
    void testFailWithResultCode() {
        Result<Void> result = Result.fail(ResultCode.UNAUTHORIZED);
        
        assertEquals(401, result.getCode());
        assertEquals("未授权，请先登录", result.getMessage());
        assertFalse(result.isSuccess());
    }

    @Test
    void testBuild() {
        Integer code = 201;
        String message = "created";
        String data = "new resource";
        
        Result<String> result = Result.build(code, message, data);
        
        assertEquals(code, result.getCode());
        assertEquals(message, result.getMessage());
        assertEquals(data, result.getData());
    }
}

