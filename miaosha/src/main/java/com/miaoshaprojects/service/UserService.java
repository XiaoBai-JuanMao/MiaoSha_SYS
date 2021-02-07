package com.miaoshaprojects.service;

import com.miaoshaprojects.error.BusinessException;
import com.miaoshaprojects.service.model.UserModel;

/**
 * @author Andy
 * @date 2020/11/14 14:13
 * @Description User业务逻辑层
 */
public interface UserService {
    UserModel getUserById(Integer id);
    void register(UserModel userModel) throws BusinessException;
    //用户注册手机telephone,用户加密后的密码encrptPassword
    UserModel validateLogin(String telephone,String encrptPassword) throws BusinessException;
}
