package com.miaoshaprojects.response;

/**
 * @author Andy
 * @date 2020/11/19 20:08
 * @Description 统一处理页面返回值 -- 正确时
 * 创建通用的返回对象是为了让前端更好的处理数据,
 * 单纯自己修改状态码不能解决这个问题,
 * 一个通用的返回对象能让前端更容易处理
 */
public class CommonReturnType {
    //表明对应请求的返回服务器处理结果 "success" 或 "fail"
    private String status;

    //若status=success,则data内返回前端需要的json数据
    //若status=fail,则data内使用通用的错误码格式
    private Object data;

    //定义一个通用的创建方法
    public static CommonReturnType create(Object result){
        return CommonReturnType.create(result,"success");
    }

    //若不带任何data，则返回success
    public static CommonReturnType create(Object result,String status){
        CommonReturnType type = new CommonReturnType();
        type.setStatus(status);
        type.setData(result);
        return type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
