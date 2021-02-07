package com.miaoshaprojects.service;

import com.miaoshaprojects.service.model.PromoModel;

/**
 * @author Andy
 * @date 2020/12/12 14:53
 * @Description 秒杀营销业务层
 */
public interface PromoService {
    //根据itemId获取即将进行或正在进行的秒杀活动
    PromoModel getPromoByItemId(Integer itemId);
}
