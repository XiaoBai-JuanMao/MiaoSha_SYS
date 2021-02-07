package com.miaoshaprojects.controller.viewobject;

import org.joda.time.DateTime;

import java.math.BigDecimal;

/**
 * @author Andy
 * @date 2020/12/3 23:21
 * @Description 商品表现层对象
 */
public class ItemVO {
    private Integer id;
    private String title; //商品名
    private BigDecimal price; //商品价格
    private Integer stock; //商品库存
    private String description; //商品描述
    private Integer sales; //商品销量
    private String imgUrl; //商品描述图片的url

    //记录商品是否在秒杀活动中，
    // 以及对应的状态 0：无秒杀活动，1：秒杀活动未开始，2：秒杀活动正在进行中
    private Integer promoStatus;
    //秒杀活动ID
    private Integer promoId;
    //秒杀活动价格
    private BigDecimal promoItemPrice;
    //秒杀活动开始时间
    private String startDate;
    public Integer getPromoStatus() {
        return promoStatus;
    }
    public void setPromoStatus(Integer promoStatus) {
        this.promoStatus = promoStatus;
    }
    public Integer getPromoId() {
        return promoId;
    }
    public void setPromoId(Integer promoId) {
        this.promoId = promoId;
    }
    public BigDecimal getPromoItemPrice() {
        return promoItemPrice;
    }
    public void setPromoItemPrice(BigDecimal promoItemPrice) {
        this.promoItemPrice = promoItemPrice;
    }
    public String getStartDate() {
        return startDate;
    }
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSales() {
        return sales;
    }

    public void setSales(Integer sales) {
        this.sales = sales;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
