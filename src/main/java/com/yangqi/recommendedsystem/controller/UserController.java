package com.yangqi.recommendedsystem.controller;

import com.yangqi.recommendedsystem.common.BusinessException;
import com.yangqi.recommendedsystem.common.CommonResult;
import com.yangqi.recommendedsystem.common.CommonUtil;
import com.yangqi.recommendedsystem.common.EmBusinessError;
import com.yangqi.recommendedsystem.model.UserModel;
import com.yangqi.recommendedsystem.request.LoginRequest;
import com.yangqi.recommendedsystem.request.RegisterRequest;
import com.yangqi.recommendedsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;

/**
 * @author xiaoer
 * @date 2020/2/22 22:51
 */
@Controller("/user")
@RequestMapping("/user")
public class UserController {
    public static final String CURRENT_USER_SESSION = "currentUserSession";

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private UserService userService;

    @RequestMapping("test")
    @ResponseBody
    public String test() {
        return "test";
    }

    @RequestMapping("/index")
    public ModelAndView index() {
        String userName = "yangqi";
        ModelAndView modelAndView = new ModelAndView("/index.html");
        modelAndView.addObject("name", userName);
        return modelAndView;
    }

    /**
     * 根据 id 获取结果
     *
     * @param id
     * @return 响应结果
     * @throws BusinessException
     */
    @RequestMapping("/get")
    @ResponseBody
    public CommonResult getUser(@RequestParam(name = "id") Integer id) throws BusinessException {
        UserModel userModel = userService.getUser(id);
        if (userModel == null) {
            // return CommonResult.create(new CommonError(EmBusinessError.NO_OBJECT_FOUND), "fail");
            throw new BusinessException(EmBusinessError.NO_OBJECT_FOUND);
        } else {
            return CommonResult.create(userModel);
        }
    }

    /**
     * 注册页面
     *
     * @param registerRequest 页面注册信息
     * @param bindingResult   错误信息
     * @return 响应结果
     * @throws BusinessException
     * @throws NoSuchAlgorithmException
     */
    @RequestMapping("/register")
    @ResponseBody
    public CommonResult register(@Valid @RequestBody RegisterRequest registerRequest, BindingResult bindingResult) throws BusinessException,
            NoSuchAlgorithmException {
        if (bindingResult.hasErrors()) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, CommonUtil.processErrorString(bindingResult));
        }

        UserModel registerUser = new UserModel();
        registerUser.setTelphone(registerRequest.getTelphone());
        registerUser.setPassword(registerRequest.getPassword());
        registerUser.setNickName(registerRequest.getNickName());
        registerUser.setGender(registerRequest.getGender());

        UserModel responseUser = userService.register(registerUser);
        return CommonResult.create(responseUser);
    }

    @RequestMapping("/login")
    @ResponseBody
    public CommonResult login(@Valid @RequestBody LoginRequest loginRequest, BindingResult bindingResult) throws BusinessException,
            NoSuchAlgorithmException {
        if (bindingResult.hasErrors()) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, CommonUtil.processErrorString(bindingResult));
        }

        UserModel userModel = userService.login(loginRequest.getTelphone(), loginRequest.getPassword());
        httpServletRequest.getSession().setAttribute(CURRENT_USER_SESSION, userModel);

        return CommonResult.create(userModel);
    }

    /**
     * 登出
     *
     * @return 响应结果
     */
    @RequestMapping("/logout")
    @ResponseBody
    public CommonResult logout() {
        // 清除掉所有的 session
        httpServletRequest.getSession().invalidate();
        return CommonResult.create(null);
    }

    /**
     * 获取当前用户信息
     *
     * @return 响应结果
     */
    @RequestMapping("/getcurrentUser")
    @ResponseBody
    public CommonResult getCurrentUser() {
        UserModel userModel = (UserModel) httpServletRequest.getSession().getAttribute(CURRENT_USER_SESSION);
        return CommonResult.create(userModel);
    }
}
