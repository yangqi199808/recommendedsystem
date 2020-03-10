package com.yangqi.recommendedsystem.controller;

import com.yangqi.recommendedsystem.common.CommonResult;
import com.yangqi.recommendedsystem.model.CategoryModel;
import com.yangqi.recommendedsystem.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author xiaoer
 * @date 2020/2/23 21:06
 */
@Controller("/category")
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @RequestMapping("/list")
    @ResponseBody
    public CommonResult list() {
        List<CategoryModel> categoryModels = categoryService.selectAll();
        return CommonResult.create(categoryModels);
    }
}
