package com.yangqi.recommendedsystem.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yangqi.recommendedsystem.common.AdminPermission;
import com.yangqi.recommendedsystem.common.BusinessException;
import com.yangqi.recommendedsystem.common.CommonUtil;
import com.yangqi.recommendedsystem.common.EmBusinessError;
import com.yangqi.recommendedsystem.model.CategoryModel;
import com.yangqi.recommendedsystem.request.CategoryCreateRequest;
import com.yangqi.recommendedsystem.request.PageQuery;
import com.yangqi.recommendedsystem.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

/**
 * @author xiaoer
 * @date 2020/2/23 20:39
 */
@Controller("/admin/category")
@RequestMapping("/admin/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 查询所有的列表品类
     *
     * @return 查询界面
     */
    @RequestMapping("/index")
    @AdminPermission
    public ModelAndView index(PageQuery pageQuery) {
        PageHelper.startPage(pageQuery.getPage(), pageQuery.getSize());
        List<CategoryModel> categoryModels = categoryService.selectAll();
        PageInfo<CategoryModel> categoryModelPageInfo = new PageInfo<>(categoryModels);

        ModelAndView modelAndView = new ModelAndView("/admin/category/index.html");
        modelAndView.addObject("data", categoryModelPageInfo);
        modelAndView.addObject("CONTROLLER_NAME", "category");
        modelAndView.addObject("ACTION_NAME", "index");
        return modelAndView;
    }

    @RequestMapping("/createpage")
    @AdminPermission
    public ModelAndView createPage() {
        ModelAndView modelAndView = new ModelAndView("/admin/category/create.html");
        modelAndView.addObject("CONTROLLER_NAME", "category");
        modelAndView.addObject("ACTION_NAME", "create");
        return modelAndView;
    }

    /**
     * 创建商家
     * 添加 @RequestBody 表示使用 json 方式去解析
     *
     * @param categoryCreateRequest 品类注册信息
     * @param bindingResult 错误信息
     * @return 重定向页面
     * @throws BusinessException
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @AdminPermission
    public String create(@Valid CategoryCreateRequest categoryCreateRequest, BindingResult bindingResult) throws BusinessException {
        if (bindingResult.hasErrors()) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, CommonUtil.processErrorString(bindingResult));
        }

        CategoryModel categoryModel = new CategoryModel();
        categoryModel.setName(categoryCreateRequest.getName());
        categoryModel.setIconUrl(categoryCreateRequest.getIconUrl());
        categoryModel.setSort(categoryCreateRequest.getSort());
        categoryService.create(categoryModel);

        return "redirect:/admin/category/index";
    }
}
