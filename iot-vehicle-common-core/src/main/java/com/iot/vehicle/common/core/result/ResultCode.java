package com.iot.vehicle.common.core.result;

import lombok.Getter;

/**
 * 返回状态码枚举
 *
 * @author dongxiang.wu
 */
@Getter
public enum ResultCode {

    /**
     * 成功
     */
    SUCCESS(200, "操作成功"),

    /**
     * 失败
     */
    FAIL(400, "操作失败"),

    /**
     * 未授权
     */
    UNAUTHORIZED(401, "未授权，请先登录"),

    /**
     * 无权限
     */
    FORBIDDEN(403, "无权限访问"),

    /**
     * 资源不存在
     */
    NOT_FOUND(404, "资源不存在"),

    /**
     * 请求方法不支持
     */
    METHOD_NOT_ALLOWED(405, "请求方法不支持"),

    /**
     * 请求参数错误
     */
    PARAM_ERROR(400, "请求参数错误"),

    /**
     * 参数校验失败
     */
    VALIDATE_FAILED(400, "参数校验失败"),

    /**
     * 服务器内部错误
     */
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),

    /**
     * 业务异常
     */
    BUSINESS_ERROR(500, "业务异常"),

    /**
     * 数据库操作失败
     */
    DATABASE_ERROR(500, "数据库操作失败"),

    /**
     * 远程调用失败
     */
    REMOTE_ERROR(500, "远程调用失败");

    /**
     * 状态码
     */
    private final Integer code;

    /**
     * 消息
     */
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}

