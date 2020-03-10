package com.yangqi.recommendedsystem.controller;

import com.yangqi.recommendedsystem.common.BusinessException;
import com.yangqi.recommendedsystem.common.CommonResult;
import com.yangqi.recommendedsystem.common.EmBusinessError;
import com.yangqi.recommendedsystem.model.CategoryModel;
import com.yangqi.recommendedsystem.model.ShopModel;
import com.yangqi.recommendedsystem.service.CategoryService;
import com.yangqi.recommendedsystem.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.util.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiaoer
 * @date 2020/2/23 23:12
 */
@Controller("/shop")
@RequestMapping("/shop")
public class ShopController {
    @Autowired
    private ShopService shopService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 推荐系统 1.0
     *
     * @param longitude 经度
     * @param latitude  维度
     * @return 推荐结果
     * @throws BusinessException
     */
    @RequestMapping("/recommend")
    @ResponseBody
    public CommonResult recommend(@RequestParam(name = "longitude") BigDecimal longitude,
                                  @RequestParam(name = "latitude") BigDecimal latitude) throws BusinessException {
        if (longitude == null || latitude == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }

        List<ShopModel> shopModels = shopService.recommend(longitude, latitude);
        return CommonResult.create(shopModels);
    }


    /**
     * 搜索系统 1.0
     *
     * @param longitude 经度
     * @param latitude  维度
     * @param keyword   搜索关键词
     * @return 推荐结果
     */
    @RequestMapping("/search")
    @ResponseBody
    public CommonResult search(@RequestParam(name = "longitude") BigDecimal longitude,
                               @RequestParam(name = "latitude") BigDecimal latitude, @RequestParam(name = "keyword") String keyword,
                               @RequestParam(name = "orderby", required = false) Integer orderby,
                               @RequestParam(name = "categoryId", required = false) Integer categoryId,
                               @RequestParam(name = "tags", required = false) String tags) throws BusinessException, IOException {
        if (StringUtils.isEmpty(keyword) || longitude == null || latitude == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }

        // List<ShopModel> shopModels = shopService.search(longitude, latitude, keyword, categoryId, orderby, tags);
        // List<ShopModel> shopModels = (List<ShopModel>) shopService.searchEs(longitude, latitude, keyword, categoryId, orderby, tags).get("shop");


        Map<String, Object> result = shopService.searchEs(longitude, latitude, keyword, categoryId, orderby, tags);
        List<ShopModel> shopModels = (List<ShopModel>) result.get("shop");
        List<CategoryModel> categoryModels = categoryService.selectAll();
        // List<Map<String, Object>> tagsAggregation = shopService.searchGroupByTags(keyword, categoryId, tags);
        List<Map<String, Object>> tagsAggregation = (List<Map<String, Object>>) result.get("tags");
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("shop", shopModels);
        resultMap.put("category", categoryModels);
        resultMap.put("tags", tagsAggregation);
        return CommonResult.create(resultMap);

    }
}
