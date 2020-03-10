package com.yangqi.recommendedsystem.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yangqi.recommendedsystem.common.AdminPermission;
import com.yangqi.recommendedsystem.common.BusinessException;
import com.yangqi.recommendedsystem.common.CommonUtil;
import com.yangqi.recommendedsystem.common.EmBusinessError;
import com.yangqi.recommendedsystem.model.ShopModel;
import com.yangqi.recommendedsystem.request.PageQuery;
import com.yangqi.recommendedsystem.request.ShopCreateRequest;
import com.yangqi.recommendedsystem.service.ShopService;
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
 * @date 2020/2/23 22:09
 */
@Controller("/admin/shop")
@RequestMapping("/admin/shop")
public class ShopController {
    @Autowired
    private ShopService shopService;

    /**
     * 查询所有的列表门店
     *
     * @return 查询界面
     */
    @RequestMapping("/index")
    @AdminPermission
    public ModelAndView index(PageQuery pageQuery) {
        PageHelper.startPage(pageQuery.getPage(), pageQuery.getSize());
        List<ShopModel> shopModels = shopService.selectAll();
        PageInfo<ShopModel> shopModelPageInfo = new PageInfo<>(shopModels);

        ModelAndView modelAndView = new ModelAndView("/admin/shop/index.html");
        modelAndView.addObject("data", shopModelPageInfo);
        modelAndView.addObject("CONTROLLER_NAME", "shop");
        modelAndView.addObject("ACTION_NAME", "index");
        return modelAndView;
    }

    @RequestMapping("/createpage")
    @AdminPermission
    public ModelAndView createPage() {
        ModelAndView modelAndView = new ModelAndView("/admin/shop/create.html");
        modelAndView.addObject("CONTROLLER_NAME", "shop");
        modelAndView.addObject("ACTION_NAME", "create");
        return modelAndView;
    }

    /**
     * 创建门店
     * 添加 @RequestBody 表示使用 json 方式去解析
     *
     * @param shopCreateRequest 门店注册信息
     * @param bindingResult 错误信息
     * @return 重定向页面
     * @throws BusinessException
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @AdminPermission
    public String create(@Valid ShopCreateRequest shopCreateRequest, BindingResult bindingResult) throws BusinessException {
        if (bindingResult.hasErrors()) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, CommonUtil.processErrorString(bindingResult));
        }

        ShopModel shopModel = new ShopModel();
        shopModel.setName(shopCreateRequest.getName());
        shopModel.setPricePerMan(shopCreateRequest.getPricePerMan());
        shopModel.setLatitude(shopCreateRequest.getLatitude());
        shopModel.setLongitude(shopCreateRequest.getLongitude());
        shopModel.setCategoryId(shopCreateRequest.getCategoryId());
        shopModel.setStartTime(shopCreateRequest.getStartTime());
        shopModel.setEndTime(shopCreateRequest.getEndTime());
        shopModel.setAddress(shopCreateRequest.getAddress());
        shopModel.setSellerId(shopCreateRequest.getSellerId());
        shopModel.setIconUrl(shopCreateRequest.getIconUrl());
        shopService.create(shopModel);

        return "redirect:/admin/shop/index";
    }
}
