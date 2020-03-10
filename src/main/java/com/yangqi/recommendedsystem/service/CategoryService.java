package com.yangqi.recommendedsystem.service;

import com.yangqi.recommendedsystem.common.BusinessException;
import com.yangqi.recommendedsystem.model.CategoryModel;

import java.util.List;

/**
 * @author xiaoer
 * @date 2020/2/23 20:26
 */
public interface CategoryService {
    /**
     * 创建品类
     *
     * @param categoryModel 创建的品类
     * @return 品类的信息
     */
    CategoryModel create(CategoryModel categoryModel) throws BusinessException;

    /**
     * 根据 id 获取品类信息
     * @param id 品类 id
     * @return 品类的信息
     */
    CategoryModel get(Integer id);

    /**
     * 查询所有的品类
     *
     * @return 品类列表
     */
    List<CategoryModel> selectAll();

    /**
     * 查询所有的品类
     *
     * @return 品类数量
     */
    Integer countAllCategory();
}
