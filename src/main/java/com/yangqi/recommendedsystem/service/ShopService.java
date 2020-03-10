package com.yangqi.recommendedsystem.service;

import com.yangqi.recommendedsystem.common.BusinessException;
import com.yangqi.recommendedsystem.model.ShopModel;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author xiaoer
 * @date 2020/2/23 21:31
 */
public interface ShopService {
    /**
     * 创建商品
     *
     * @param shopModel 要创建的商品
     * @return 商品信息
     */
    ShopModel create(ShopModel shopModel) throws BusinessException;

    /**
     * 根据 id 获取商品
     *
     * @param id 商品 id
     * @return 商品信息
     */
    ShopModel get(Integer id);

    /**
     * 获取所有商品
     *
     * @return 商品列表
     */
    List<ShopModel> selectAll();

    /**
     * 推荐系统 1.0
     *
     * @param longitude 经度
     * @param latitude  维度
     * @return 推荐列表
     */
    List<ShopModel> recommend(BigDecimal longitude, BigDecimal latitude);

    /**
     * 关键词，标签聚合
     *
     * @param keyword 关键字
     * @param categoryId 分类
     * @param tags 标签
     * @return 推荐列表
     */
    List<Map<String, Object>> searchGroupByTags(String keyword, Integer categoryId, String tags);

    /**
     * 查询所有门店
     *
     * @return 门店数量
     */
    Integer countAllShop();

    /**
     * 搜索服务 1.0
     *
     * @param longitude  经度
     * @param latitude   维度
     * @param keyword    关键词
     * @param categoryId 分类
     * @param orderby    排序
     * @param tags 标签
     * @return 搜索结果列表
     */
    List<ShopModel> search(BigDecimal longitude, BigDecimal latitude, String keyword, Integer categoryId, Integer orderby, String tags);

    /**
     * 搜索服务 2.0
     *
     * @param longitude 经度
     * @param latitude 维度
     * @param keyword 关键词
     * @param categoryId 分类
     * @param orderby 排序
     * @param tags 标签
     * @return 搜索结果
     */
    Map<String, Object> searchEs(BigDecimal longitude, BigDecimal latitude, String keyword, Integer categoryId, Integer orderby,
                                String tags) throws IOException;
}
