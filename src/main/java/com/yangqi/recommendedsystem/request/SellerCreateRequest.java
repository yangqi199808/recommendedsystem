package com.yangqi.recommendedsystem.request;

import javax.validation.constraints.NotBlank;

/**
 * @author xiaoer
 * @date 2020/2/23 18:51
 */
public class SellerCreateRequest {
    @NotBlank(message = "商户名称不能为空")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
