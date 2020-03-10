package com.yangqi.recommendedsystem.common;

/**
 * @author xiaoer
 * @date 2020/2/23 9:35
 */
public class BusinessException extends Exception {
    private CommonError commonError;

    public BusinessException(EmBusinessError emBusinessError) {
        super();
        this.commonError = new CommonError(emBusinessError);
    }

    public BusinessException(EmBusinessError emBusinessError, String errorMessage) {
        super();
        this.commonError = new CommonError(emBusinessError);
        this.commonError.setErrorMessage(errorMessage);
    }

    public CommonError getCommonError() {
        return commonError;
    }

    public void setCommonError(CommonError commonError) {
        this.commonError = commonError;
    }
}
