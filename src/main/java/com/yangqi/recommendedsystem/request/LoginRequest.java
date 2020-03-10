package com.yangqi.recommendedsystem.request;

import javax.validation.constraints.NotBlank;

/**
 * @author xiaoer
 * @date 2020/2/23 12:58
 */
public class LoginRequest {
    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    private String telphone;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
