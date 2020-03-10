package com.yangqi.recommendedsystem.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author xiaoer
 * @date 2020/2/23 20:44
 */
public class CategoryCreateRequest {
    /**
     * 品类名称
     */
    @NotBlank(message = "品类名称不能为空")
    private String name;

    /**
     * 品类图标 url
     */
    @NotBlank(message = "品类 url 不能为空")
    private String iconUrl;

    /**
     * 品类排序权重
     */
    @NotNull(message = "品类排序权重不能为空")
    private Integer sort;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
