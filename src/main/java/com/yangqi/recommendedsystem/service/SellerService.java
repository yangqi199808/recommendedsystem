package com.yangqi.recommendedsystem.service;

import com.yangqi.recommendedsystem.common.BusinessException;
import com.yangqi.recommendedsystem.model.SellerModel;

import java.util.List;

/**
 * @author xiaoer
 * @date 2020/2/23 18:24
 */
public interface SellerService {
    /**
     * 创建商家
     *
     * @param sellerModel 要创建的商家
     * @return 创建商家的信息
     */
    SellerModel create(SellerModel sellerModel);

    /**
     * 通过 id 查询商户
     *
     * @param id 商户 id
     * @return 商户的信息
     */
    SellerModel get(Integer id);

    /**
     * 查询所有的商户信息
     *
     * @return 商户信息列表
     */
    List<SellerModel> selectAll();

    /**
     * 商户是否启用或停用
     *
     * @param id 商户 id
     * @param disabledFlag 商户启停标志
     * @return 商户信息
     */
    SellerModel changeStatus(Integer id, Integer disabledFlag) throws BusinessException;

    /**
     * 查询商户数量
     *
     * @return 商户数量
     */
    Integer countAllSeller();
}
