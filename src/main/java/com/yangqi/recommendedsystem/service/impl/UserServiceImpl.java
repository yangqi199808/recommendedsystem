package com.yangqi.recommendedsystem.service.impl;

import com.yangqi.recommendedsystem.common.BusinessException;
import com.yangqi.recommendedsystem.common.EmBusinessError;
import com.yangqi.recommendedsystem.dal.UserModelMapper;
import com.yangqi.recommendedsystem.model.UserModel;
import com.yangqi.recommendedsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.misc.BASE64Encoder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 * @author xiaoer
 * @date 2020/2/22 23:02
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserModelMapper userModelMapper;

    @Override
    public UserModel getUser(Integer id) {
        return userModelMapper.selectByPrimaryKey(id);
    }

    /**
     * 注册
     *
     * @param registerUser 要注册的用户
     * @return 返回注册后的用户结果
     * @throws BusinessException
     * @throws NoSuchAlgorithmException
     */
    @Override
    @Transactional
    public UserModel register(UserModel registerUser) throws BusinessException, NoSuchAlgorithmException {
        registerUser.setPassword(encodeByMd5(registerUser.getPassword()));
        registerUser.setCreateAt(new Date());
        registerUser.setUpdateAt(new Date());

        try {
            userModelMapper.insertSelective(registerUser);
        } catch (DuplicateKeyException ex) {
            throw new BusinessException(EmBusinessError.REGISTER_DUP_FAIL);
        }
        return getUser(registerUser.getId());
    }

    /**
     * 登录
     *
     * @param telphone 登录账户：手机号
     * @param password 登录密码
     * @return 返回登录后的用户结果
     * @throws NoSuchAlgorithmException
     * @throws BusinessException
     */
    @Override
    public UserModel login(String telphone, String password) throws NoSuchAlgorithmException, BusinessException {
        UserModel userModel = userModelMapper.selectByTelphoneAndPassword(telphone, encodeByMd5(password));
        if (userModel == null) {
            throw new BusinessException(EmBusinessError.LOGIN_FAIL);
        }
        return userModel;
    }

    /**
     * 查询用户总数
     *
     * @return 用户总数量
     */
    @Override
    public Integer countAllUser() {
        return userModelMapper.countAllUser();
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
