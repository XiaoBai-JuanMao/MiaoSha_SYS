package com.miaoshaprojects.controller;

import com.alibaba.druid.util.StringUtils;
import com.miaoshaprojects.controller.viewobject.UserVO;
import com.miaoshaprojects.error.BusinessException;
import com.miaoshaprojects.error.EmBusinessError;
import com.miaoshaprojects.response.CommonReturnType;
import com.miaoshaprojects.service.model.UserModel;
import com.miaoshaprojects.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * @author Andy
 * @date 2020/11/14 14:10
 * @Description user表现层
 */
//使用Controller标记，被Spring扫描到
@Controller("user")
//设置url访问方式
@RequestMapping("/user")
//SpringBoot配置允许跨源请求
// -- 默认方式不能做到session共享，需指定两个参数，使后端授信
// -- 配合前端xhrFields 使前端授信，实现session共享
@CrossOrigin(allowCredentials="true",allowedHeaders="*")
public class UserController extends BaseController {
    @Autowired
    private UserService userService;
    //通过bean方式注入HttpServletRequest，属于单例模式，理应不支持多用户访问
    //但是SpringBean包装后的HttpServletRequest本质上是一个RequestObjectFactory，
    //内部拥有ThreadLocal方式的map，让用户在自己的线程中处理自己的request
    @Autowired
    private HttpServletRequest httpServletRequest;

    //【用户登录接口】
    @RequestMapping(value = "/login",method = {RequestMethod.POST},consumes={CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType login(@RequestParam(name="telephone")String telephone,
                                  @RequestParam(name="password")String password)
            throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        //入参校验
        if (org.apache.commons.lang3.StringUtils.isEmpty(telephone)
                || org.apache.commons.lang3.StringUtils.isEmpty(password)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }

        //用户登录服务，校验用户登录是否合法
        String encrptPassword = EncodeByMd5(password);
        UserModel userModel = userService.validateLogin(telephone,encrptPassword);
        UserVO userVO = convertFromModelObject(userModel);

        //将登陆凭证加入到用户登录成功的session内 -- 一般用TOKEN，此处使用用户的session
        this.httpServletRequest.getSession().setAttribute("IS_LOGIN",true);
        this.httpServletRequest.getSession().setAttribute("LOGIN_USER",userVO);

        return CommonReturnType.create(null);
    }

    //【用户注册接口】
    @RequestMapping(value = "/register",method = {RequestMethod.POST},consumes={CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType register(@RequestParam(name="telephone")String telephone,
                                     @RequestParam(name="otpCode")String otpCode,
                                     @RequestParam(name="name")String name,
                                     @RequestParam(name="password")String password,
                                     @RequestParam(name="gender")Integer gender,
                                     @RequestParam(name="age")Integer age)
            throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        //验证手机号是否和对应的otpCode相符合
        //实现session共享，需配置前端xhrFields，后端allowCredentials
        String inSessionOtpCode = (String) this.httpServletRequest.getSession().getAttribute(telephone);
        if (!StringUtils.equals(otpCode,inSessionOtpCode)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "短信验证码不正确");
        }

        //用户注册流程
        UserModel userModel = new UserModel();
        userModel.setName(name);
        userModel.setGender(new Byte(String.valueOf(gender.intValue())));
        userModel.setAge(age);
        userModel.setTelephone(telephone);
        userModel.setRegisterMode("byphone");
        userModel.setEncrptPasssword(EncodeByMd5(password));

        userService.register(userModel);
        return CommonReturnType.create(null);
    }

    //用户密码加密
    public String EncodeByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //确定计算方法
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64en = new BASE64Encoder();
        //加密字符串
        String newString = base64en.encode(md5.digest(str.getBytes("utf-8")));
        return newString;
    }

    //用户【获取otp短信】接口
    @RequestMapping(value = "/getotp",method = {RequestMethod.POST},consumes={CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType getOtp(@RequestParam(name="telephone")String telephone){
        //需要按照一定的规则生成OTP验证码
        Random random = new Random();
        int randomInt = random.nextInt(99999);
        randomInt += 10000;
        String otpCode = String.valueOf(randomInt);

        //将OTP验证码同对应用户的手机号关联 -- （建议）使用redis自带的keyValue建立关联
        //此处使用HTTPSession的方式绑定手机号与otpCode
        HttpSession session = httpServletRequest.getSession();
        session.setMaxInactiveInterval(60*2); //2分钟
        session.setAttribute(telephone,otpCode);

        //将OTP验证码通过短信通道发送给用户，此处省略
        System.out.println("telephone = " + telephone + " & otpCode = "+otpCode);

        return CommonReturnType.create(null);
    }

    @RequestMapping("/get")
    @ResponseBody
    //调用service服务获取对应id的user对象，并返回给前端
    public CommonReturnType getUser(@RequestParam(name = "id") Integer id) throws BusinessException {
        UserModel userModel = userService.getUserById(id);
        //将业务逻辑层对象转换为可供UI使用的表现层对象返回前端
        UserVO userVO = convertFromModelObject(userModel);

        //若获取的对应用户信息不存在
        if (userModel == null){
            userModel.setEncrptPasssword("123"); //未知错误
            //throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }

        //返回通用对象
        return CommonReturnType.create(userVO);
    }

    //业务逻辑层对象转换为表现层对象
    public UserVO convertFromModelObject(UserModel userModel){
        if (userModel==null){
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userModel,userVO);
        return userVO;
    }
}
