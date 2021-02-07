package com.miaoshaprojects.controller;

import com.miaoshaprojects.controller.viewobject.UserVO;
import com.miaoshaprojects.error.BusinessException;
import com.miaoshaprojects.error.EmBusinessError;
import com.miaoshaprojects.response.CommonReturnType;
import com.miaoshaprojects.service.OrderService;
import com.miaoshaprojects.service.model.OrderModel;
import com.miaoshaprojects.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Andy
 * @date 2020/12/11 21:13
 * @Description 订单
 */
@Controller("order")
@RequestMapping("/order")
@CrossOrigin(allowCredentials="true",allowedHeaders="*")
public class OrderController extends BaseController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private HttpServletRequest httpServletRequest;

    //封装下单请求
    @RequestMapping(value = "/create",method = {RequestMethod.POST},consumes={CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType createOrder(@RequestParam(name="itemId")Integer itemId,
                                        @RequestParam(name="amount")Integer amount,
                                        @RequestParam(name="promoId",required = false)Integer promoId) throws BusinessException {
        //获取用户的登陆信息
        //不能使用boolean，应使用Boolean,否则可能报空指针异常
        Boolean is_login = (Boolean) this.httpServletRequest.getSession().getAttribute("IS_LOGIN");
        if (is_login==null || !is_login.booleanValue()){
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN);
        }
        UserVO userVO = (UserVO) this.httpServletRequest.getSession().getAttribute("LOGIN_USER");

        OrderModel orderModel = orderService.createOrder(userVO.getId(), itemId, promoId, amount);
        return CommonReturnType.create(null);
    }
}
