package com.yangqi.recommendedsystem.recommended;

/**
 * @author Yankee
 * @date 2020/3/11 10:21
 */
public class ShopSortModel {
    private Integer shopId;
    private double score;

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
