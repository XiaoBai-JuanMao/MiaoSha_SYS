package com.miaoshaprojects.service.impl;

import com.miaoshaprojects.dao.ItemDOMapper;
import com.miaoshaprojects.dao.OrderDOMapper;
import com.miaoshaprojects.dao.SequenceDOMapper;
import com.miaoshaprojects.dataobject.OrderDO;
import com.miaoshaprojects.dataobject.SequenceDO;
import com.miaoshaprojects.error.BusinessException;
import com.miaoshaprojects.error.EmBusinessError;
import com.miaoshaprojects.service.ItemService;
import com.miaoshaprojects.service.OrderService;
import com.miaoshaprojects.service.UserService;
import com.miaoshaprojects.service.model.ItemModel;
import com.miaoshaprojects.service.model.OrderModel;
import com.miaoshaprojects.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Andy
 * @date 2020/12/10 19:42
 * @Description 订单
 */
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    ItemService itemService;
    @Autowired
    UserService userService;
    @Autowired
    OrderDOMapper orderDOMapper;
    @Autowired
    SequenceDOMapper sequenceDOMapper;
    @Autowired
    ItemDOMapper itemDOMapper;

    @Override
    @Transactional
    public OrderModel createOrder(Integer userId, Integer itemId,Integer promoId, Integer amount) throws BusinessException {
        //1.校验下单状态：用户是否合法，下单商品是否存在，购买数量是否正确
        UserModel userModel = userService.getUserById(userId);
        if (userModel==null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"用户信息不存在");
        }
        ItemModel itemModel = itemService.getItemById(itemId);
        if (itemModel==null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"商品信息不存在");
        }
        if (amount<=0 || amount>99){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"购买数量不正确");
        }

        //校验活动信息
        if (promoId!=null){
            //1.校验对应活动是否存在这个使用商品
            if (promoId.intValue()!=itemModel.getPromoModel().getId()){
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"活动信息不正确");
                //2.校验活动是否正在进行中
            }else if (itemModel.getPromoModel().getStatus().intValue() !=2){
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"活动还未开始");
            }
        }

        //2.落单减库存（另一种方式：支付减库存）
        boolean result = itemService.decreaseStock(itemId,amount);
        if (!result){
            throw new BusinessException(EmBusinessError.STOKE_NOT_ENOUGH);
        }
        //3.订单入库
        OrderModel orderModel = new OrderModel();
        orderModel.setUserId(userId);
        orderModel.setItemId(itemId);
        orderModel.setAmount(amount);
        if (promoId!=null){
            orderModel.setItemPrice(itemModel.getPromoModel().getPromoItemPrice());
        }else {
            orderModel.setItemPrice(itemModel.getPrice());
        }
        orderModel.setPromId(promoId);
        //BigDecimal的乘法使用multiply
        orderModel.setOrderPrice(orderModel.getItemPrice().multiply(new BigDecimal(amount)));

        //生成交易流水号(订单号)
        orderModel.setId(generateOrderNo(userId));

        OrderDO orderDO = convertFromOrderModel(orderModel);
        orderDOMapper.insertSelective(orderDO);

        //更新商品销量
        itemDOMapper.increaseSales(itemId,amount);
        
        //4.返回前端
        return orderModel;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    //生成交易流水号(订单号)
    String generateOrderNo(int userId){
        //订单号16位
        StringBuilder stringBuilder = new StringBuilder();
        //前8位为时间信息，年月日
        LocalDateTime now = LocalDateTime.now();
        String nowDate = now.format(DateTimeFormatter.ISO_DATE).replace("-", "");
        stringBuilder.append(nowDate);
        //中间6位为自增序列（即同一天下单的不同订单）
        //通过建一张表来实现自增序列
        //就算订单事务失败回滚，sequence也不应该回滚，被重复使用，这是为了保证全局唯一性
        //解决方法 事务中开启新事务 @Transactional(propagation = Propagation.REQUIRES_NEW)
        int sequence = 0;
        SequenceDO sequenceDO = sequenceDOMapper.getSequenceByName("order_info");
        sequence = sequenceDO.getCurrentValue();
        sequenceDO.setCurrentValue(sequenceDO.getCurrentValue()+sequenceDO.getStep());
        sequenceDOMapper.updateByPrimaryKeySelective(sequenceDO);
        String sequenceStr = String.format("%06d", sequence);
        stringBuilder.append(sequenceStr);
        //最后2位为分库分表位
        String sub = String.format("%02d", userId % 100);
        stringBuilder.append(sub);
        return stringBuilder.toString();
    }

    private OrderDO convertFromOrderModel(OrderModel orderModel){
        if (orderModel==null){
            return null;
        }
        OrderDO orderDO = new OrderDO();
        BeanUtils.copyProperties(orderModel,orderDO);
        orderDO.setItemPrice(orderModel.getItemPrice().doubleValue());
        orderDO.setOrderPrice(orderModel.getOrderPrice().doubleValue());
        return orderDO;
    }
}
