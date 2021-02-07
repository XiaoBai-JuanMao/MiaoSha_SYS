package com.miaoshaprojects.service.model;

import java.math.BigDecimal;

/**
 * @author Andy
 * @date 2020/12/10 19:10
 * @Description 用户下单交易模型
 */
public class OrderModel {
    //订单号：2020121000013215
    private String id;
    private Integer userId; //用户id
    private Integer itemId; //商品id
    //若非空，则表示是以秒杀商品方式下单
    private Integer promId; //秒杀活动id
    //购买商品的单价，若promoId非空，则表示秒杀商品价格
    private BigDecimal itemPrice;   //当时购买商品的单价
    private Integer amount; //购买商品数量
    //若promoId非空，则表示秒杀商品价格
    private BigDecimal orderPrice;  //购买金额

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getPromId() {
        return promId;
    }

    public void setPromId(Integer promId) {
        this.promId = promId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public BigDecimal getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(BigDecimal itemPrice) {
        this.itemPrice = itemPrice;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }
}
