package com.yangqi.recommendedsystem.controller.admin;

import com.yangqi.recommendedsystem.common.AdminPermission;
import com.yangqi.recommendedsystem.common.BusinessException;
import com.yangqi.recommendedsystem.common.EmBusinessError;
import com.yangqi.recommendedsystem.service.CategoryService;
import com.yangqi.recommendedsystem.service.SellerService;
import com.yangqi.recommendedsystem.service.ShopService;
import com.yangqi.recommendedsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.util.StringUtils;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author xiaoer
 * @date 2020/2/23 14:19
 */
@Controller("/admin/admin")
@RequestMapping("/admin/admin")
public class AdminController {
    @Value("${admin.email}")
    private String email;

    @Value("${admin.encryptPassword}")
    private String encryptPassword;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private SellerService sellerService;

    public static final String CURRENT_ADMIN_SESSION = "currentAdminSession";

    @RequestMapping("/index")
    @AdminPermission
    // @ResponseBody
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("/admin/admin/index");
        modelAndView.addObject("userCount", userService.countAllUser());
        modelAndView.addObject("shopCount", shopService.countAllShop());
        modelAndView.addObject("sellerCount", sellerService.countAllSeller());
        modelAndView.addObject("categoryCount", categoryService.countAllCategory());
        modelAndView.addObject("CONTROLLER_NAME", "admin");
        modelAndView.addObject("ACTION_NAME", "index");
        return modelAndView;
        // return CommonResult.create(null);
    }

    @RequestMapping("/loginpage")
    public ModelAndView loginpage() {
        ModelAndView modelAndView = new ModelAndView("/admin/admin/login");
        return modelAndView;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@RequestParam(name = "email")String email, @RequestParam(name = "password")String password) throws BusinessException,
            NoSuchAlgorithmException {
        if (StringUtils.isEmpty(email) || StringUtils.isEmpty(password)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "用户名或密码不能为空");
        }

        if (email.equals(this.email) && encodeByMd5(password).equals(this.encryptPassword)) {
            // 登录成功
            httpServletRequest.getSession().setAttribute(CURRENT_ADMIN_SESSION, email);
            return "redirect:/admin/admin/index";
        } else {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "用户名或密码错误");
        }
    }

    /**
     * 对 password 进行加密
     *
     * @param string 明文 password
     * @return 密文 password
     * @throws NoSuchAlgorithmException
     */
    private String encodeByMd5(String string) throws NoSuchAlgorithmException {
        // 确定计算方法：MD5
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        BASE64Encoder base64Encoder = new BASE64Encoder();
        return base64Encoder.encode(messageDigest.digest(string.getBytes(StandardCharsets.UTF_8)));
    }
}
