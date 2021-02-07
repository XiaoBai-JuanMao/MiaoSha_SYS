package com.miaoshaprojects.controller;

import com.miaoshaprojects.error.BusinessException;
import com.miaoshaprojects.error.EmBusinessError;
import com.miaoshaprojects.response.CommonReturnType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Andy
 * @date 2020/11/30 17:05
 * @Description 通用Controller，解决未被controller层吸收的exception
 */
public class BaseController {
    //声明前端表单的contentType
    public static final String CONTENT_TYPE_FORMED="application/x-www-form-urlencoded";

    //SpringBoot定义exceptionhandler解决未被controller层吸收的exception
    @ExceptionHandler(Exception.class)
    //controller层抛出的问题是业务逻辑的问题，不应该是500服务端不能处理的问题
    @ResponseStatus(HttpStatus.OK)
    //返回自定义格式的数据{status："",data:{}}
    @ResponseBody
    public Object handlerException(HttpServletRequest request, Exception ex){
        Map<String,Object> responseData = new HashMap<>();
        if (ex instanceof BusinessException){
            //返回自定义的exception的data数据
            BusinessException businessException = (BusinessException) ex;
            responseData.put("errCode",businessException.getErrCode());
            responseData.put("errMsg",businessException.getErrMsg());
        }else {
            responseData.put("errCode", EmBusinessError.UNKNOWN_ERROR.getErrCode());
            responseData.put("errMsg",EmBusinessError.UNKNOWN_ERROR.getErrMsg());
        }

        return CommonReturnType.create(responseData,"fail");
    }
}
