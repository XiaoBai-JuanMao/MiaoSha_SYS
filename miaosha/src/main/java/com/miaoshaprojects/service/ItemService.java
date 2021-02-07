package com.miaoshaprojects.service;

import com.miaoshaprojects.error.BusinessException;
import com.miaoshaprojects.service.model.ItemModel;

import java.util.List;

/**
 * @author Andy
 * @date 2020/12/3 22:27
 * @Description 商品业务逻辑层
 */
public interface ItemService {
    //创建商品
    ItemModel createItem(ItemModel itemModel) throws BusinessException;
    //商品列表浏览
    List<ItemModel> listItem();
    //商品详情浏览
    ItemModel getItemById(Integer id);
    //库存扣减
    boolean decreaseStock(Integer itemId,Integer amount) throws BusinessException;
    //销量增加
    void increaseSales(Integer itemId,Integer amount);
}
