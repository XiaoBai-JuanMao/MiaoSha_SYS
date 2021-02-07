package com.miaoshaprojects.service.impl;

import com.miaoshaprojects.dao.UserDOMapper;
import com.miaoshaprojects.dao.UserPasswordDOMapper;
import com.miaoshaprojects.dataobject.UserDO;
import com.miaoshaprojects.dataobject.UserPasswordDO;
import com.miaoshaprojects.error.BusinessException;
import com.miaoshaprojects.error.EmBusinessError;
import com.miaoshaprojects.service.model.UserModel;
import com.miaoshaprojects.service.UserService;
import com.miaoshaprojects.validator.ValidationResult;
import com.miaoshaprojects.validator.ValidatorImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Andy
 * @date 2020/11/14 14:13
 * @Description TODO
 */
//指定为Spring的Service
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDOMapper userDOMapper;
    @Autowired
    private UserPasswordDOMapper userPasswordDOMapper;
    @Autowired
    private ValidatorImpl validator;

    @Override
    public UserModel getUserById(Integer id) {
        //调用userDOMapper获取对应的用户dataObject
        UserDO userDO = userDOMapper.selectByPrimaryKey(id);
        if (userDO==null){
            return null;
        }
        //通过userId获取用户加密密码信息
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());
        //整合、转换为业务逻辑使用的Model类
        return convertFromDataObject(userDO,userPasswordDO);
    }

    //【用户登录验证】
    @Override
    @Transactional
    public UserModel validateLogin(String telephone, String encrptPassword) throws BusinessException {
        //通过用户数据获取用户信息
        UserDO userDO = userDOMapper.selectByTelephone(telephone);
        if (userDO == null){
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());
        UserModel userModel = convertFromDataObject(userDO,userPasswordDO);

        //比对用户信息内加密的密码是否和传输尽量的密码相同
        if (!com.alibaba.druid.util.StringUtils.equals(encrptPassword,userModel.getEncrptPasssword())){
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }
        return userModel;
    }

    //【用户注册】
    @Override
    //添加事务
    @Transactional
    public void register(UserModel userModel) throws BusinessException {
        if (userModel == null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }

//        if (StringUtils.isEmpty(userModel.getName())
//                || userModel.getGender() == null
//                || userModel.getAge() == null
//                || StringUtils.isEmpty(userModel.getTelephone())){
//            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
//        }
        //校验优化
        ValidationResult result = validator.validate(userModel);
        if (result.isHasErrors()){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
        }

        //实现model对象转dataobject的方法
        UserDO userDO = convertFromModel(userModel);
        //因为telephone是唯一索引，当添加相同telephone时，报错
        try {
            userDOMapper.insertSelective(userDO);
        }catch (DuplicateKeyException e){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"手机号已重复注册");
        }

        //需在UserDOMapper.xml查询字段内配置keyProperty="id" useGeneratedKeys="true
        //获取userDO的自增id,赋值给userModel
        userModel.setId(userDO.getId());

        UserPasswordDO userPasswordDO = convertPasswordFromModel(userModel);
        userPasswordDOMapper.insertSelective(userPasswordDO);

        return;
    }

    //业务逻辑层对象service转换为数据层对象dao
    private UserDO convertFromModel(UserModel userModel){
        if (userModel==null){
            return null;
        }
        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(userModel,userDO);
        return userDO;
    }
    private UserPasswordDO convertPasswordFromModel(UserModel userModel){
        if (userModel==null){
            return null;
        }
        UserPasswordDO userPasswordDO = new UserPasswordDO();
        userPasswordDO.setEncrptPasssword(userModel.getEncrptPasssword());
        userPasswordDO.setUserId(userModel.getId());
        return userPasswordDO;
    }

    //数据层对象dao转换为业务逻辑层对象service
    private UserModel convertFromDataObject(UserDO userDO, UserPasswordDO userPasswordDO){
        if (userDO==null){
            return null;
        }
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userDO,userModel);
        if (userPasswordDO!=null){
            userModel.setEncrptPasssword(userPasswordDO.getEncrptPasssword());
        }
        return userModel;
    }
}
