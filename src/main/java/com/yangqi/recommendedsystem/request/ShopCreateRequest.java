package com.yangqi.recommendedsystem.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author xiaoer
 * @date 2020/2/23 22:14
 */
public class ShopCreateRequest {
    /**
     * 门店名称
     */
    @NotBlank(message = "门店名称不能为空")
    private String name;

    /**
     * 人均价格
     */
    @NotNull(message = "人均价格不能为空")
    private Integer pricePerMan;

    /**
     * 维度
     */
    @NotNull(message = "维度不能为空")
    private BigDecimal latitude;

    /**
     * 经度
     */
    @NotNull(message = "经度不能为空")
    private BigDecimal longitude;

    /**
     * 服务类目 id
     */
    @NotNull(message = "服务类目不能为空")
    private Integer categoryId;

    /**
     * 标签
     */
    private String tags;

    /**
     * 开始营业时间
     */
    @NotBlank(message = "开始营业时间不能为空")
    private String startTime;

    /**
     * 结束营业时间
     */
    @NotBlank(message = "结束营业时间不能为空")
    private String endTime;

    /**
     * 门店地址
     */
    @NotBlank(message = "门店地址不能为空")
    private String address;

    /**
     * seller id
     */
    @NotNull(message = "商家 id 不能为空")
    private Integer sellerId;

    /**
     * 门店图片地址
     */
    @NotBlank(message = "门店照片不能为空")
    private String iconUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPricePerMan() {
        return pricePerMan;
    }

    public void setPricePerMan(Integer pricePerMan) {
        this.pricePerMan = pricePerMan;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
}
