package com.miaoshaprojects.validator;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Andy
 * @date 2020/12/2 19:52
 * @Description 优化校验规则
 */
public class ValidationResult {
    //校验结果是否有错
    private boolean hasErrors = false;
    //存放错误信息的map
    private Map<String,String> errorMsgMap = new HashMap<>();

    public boolean isHasErrors() {
        return hasErrors;
    }

    public void setHasErrors(boolean hasErrors) {
        this.hasErrors = hasErrors;
    }

    public Map<String, String> getErrorMsgMap() {
        return errorMsgMap;
    }

    public void setErrorMsgMap(Map<String, String> errorMsgMap) {
        this.errorMsgMap = errorMsgMap;
    }

    //实现通用的通过格式化字符串信息获取粗偶结果的msg方法
    public String getErrMsg(){
        //把数组以，分隔 ，并返回字符串
        return StringUtils.join( errorMsgMap.values().toArray(),",");
    }
}
