package com.miaoshaprojects.service.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author Andy
 * @date 2020/12/3 20:26
 * @Description 商品表现层
 */
public class ItemModel {
    private Integer id;
    @NotBlank(message = "商品名不能为空")
    private String title; //商品名
    //Double传到前端会存在精度问题（1.9->1.99999），只能使用BigDecimal
    @NotNull(message = "商品价格不能为空")
    @Min(value = 0,message = "商品价格必须大于0")
    private BigDecimal price; //商品价格
    @NotNull(message = "商品库存不能为空")
    private Integer stock; //商品库存
    @NotBlank(message = "商品描述不能为空")
    private String description; //商品描述
    //销量非入参范围，通过其他操作来添加
    private Integer sales; //商品销量
    @NotBlank(message = "商品图片信息不能为空")
    private String imgUrl; //

    //使用聚合模型 -- 若promoModel不为空，则表示其拥有还未结束的秒杀活动(1,2)
    private PromoModel promoModel;
    public PromoModel getPromoModel() {
        return promoModel;
    }
    public void setPromoModel(PromoModel promoModel) {
        this.promoModel = promoModel;
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
