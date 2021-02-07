package com.miaoshaprojects.error;

/**
 * @author Andy
 * @date 2020/11/19 20:08
 * @Description 统一处理页面返回值 -- 错误时
 * 创建通用的返回对象是为了让前端更好的处理数据,
 * 单纯自己修改状态码不能解决这个问题,
 * 一个通用的返回对象能让前端更容易处理
 */
public interface CommonError {
    public int getErrCode();
    public String getErrMsg();
    public CommonError setErrMsg(String errMsg);


}
