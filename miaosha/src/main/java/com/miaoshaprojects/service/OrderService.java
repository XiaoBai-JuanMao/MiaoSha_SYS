package com.miaoshaprojects.service;

import com.miaoshaprojects.error.BusinessException;
import com.miaoshaprojects.service.model.OrderModel;

/**
 * @author Andy
 * @date 2020/12/10 19:40
 * @Description 订单
 */
public interface OrderService {
    //方案
    //1.通过前端url上，传过来秒杀活动id，然后下单接口内校验对应id是否属于对应商品且活动已开始
    //2.直接在下单接口内判断对应的商品是否存在秒杀活动，若存在进行中的则以秒杀价格下单
    //选择第一种方案
    // 优点
    //1.业务逻辑上的模型可扩展性 -- 对应的一个商品可能会同一时间存在多个秒杀活动（如不同app中）
    //2.若判断商品是否存在秒杀活动，则任何一个非秒杀的商品，也要查询活动对应的信息，不利于下单接口性能
    OrderModel createOrder(Integer userId,Integer itemId,Integer promoId,Integer amount) throws BusinessException;
}
