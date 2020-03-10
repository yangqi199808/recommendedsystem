package com.yangqi.recommendedsystem.common;

/**
 * @author xiaoer
 * @date 2020/2/23 9:24
 */
public enum EmBusinessError {
    /**
     * 通用的错误类型为 1000 开头
     */
    NO_OBJECT_FOUND(10001, "请求对象不存在"),
    UNKNOWN_ERROR(10002, "未知错误"),
    NO_HANDLER_FOUND(10003, "找不到执行的路径操作"),
    BIND_EXCEPTION_ERROR(10004, "请求参数错误"),
    PARAMETER_VALIDATION_ERROR(10005, "请求参数校验失败"),

    /**
     * 用户服务相关的错误类型 2000 开头
     */
    REGISTER_DUP_FAIL(20001, "用户已存在"),
    LOGIN_FAIL(20002, "手机号或密码错误"),

    /**
     * admin 相关错误类型 3000 开头
     */
    ADMIN_SHOULD_LOGIN(30001, "请先进行登录操作"),

    /**
     * 品类相关错误 4000 开头
     */
    CATEGORY_NAME_DUPLICATED(40001, "品类名称已经存在"),;

    /**
     * 错误码
     */
    private Integer errorCode;

    /**
     * 错误描述
     */
    private String errorMessage;

    EmBusinessError(Integer errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
