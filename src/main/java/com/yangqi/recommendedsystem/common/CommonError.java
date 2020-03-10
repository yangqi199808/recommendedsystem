package com.yangqi.recommendedsystem.common;

/**
 * @author xiaoer
 * @date 2020/2/23 9:21
 */
public class CommonError {
    /**
     * 错误码
     */
    private Integer errorCode;

    /**
     * 错误描述
     */
    private String errorMessage;

    public CommonError(Integer errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public CommonError(EmBusinessError emBusinessError) {
        this.errorCode = emBusinessError.getErrorCode();
        this.errorMessage = emBusinessError.getErrorMessage();
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
