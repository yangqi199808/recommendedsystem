package com.yangqi.recommendedsystem.service;

import com.yangqi.recommendedsystem.common.BusinessException;
import com.yangqi.recommendedsystem.model.UserModel;

import java.security.NoSuchAlgorithmException;

/**
 * @author xiaoer
 * @date 2020/2/22 22:51
 */
public interface UserService {
    /**
     * 根据 id 获取用户
     *
     * @param id 用户 id
     * @return User
     */
    UserModel getUser(Integer id);

    /**
     * 用户注册
     *
     * @param registerUser 要注册的用户
     * @return 注册完成的用户
     */
    UserModel register(UserModel registerUser) throws BusinessException, NoSuchAlgorithmException;

    /**
     * 用户登录
     *
     * @param telphone 登录账户：手机号
     * @param password 登录密码
     * @return 登录完成后的用户信息
     */
    UserModel login(String telphone, String password) throws NoSuchAlgorithmException, BusinessException;

    /**
     * 获取所有用户数量
     *
     * @return 用户数量
     */
    Integer countAllUser();
}
