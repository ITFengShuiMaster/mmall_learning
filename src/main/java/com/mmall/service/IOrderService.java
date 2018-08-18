package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;

import java.util.Map;

/**
 * @author Luyue
 * @date 2018/8/4 14:42
 **/
public interface IOrderService {

    ServerResponse create(Integer userId, Integer shippingId);

    ServerResponse cancel(Integer userId, Long orderNo);

    ServerResponse getCartProduct(Integer userId);

    ServerResponse detail(Integer userId, Long orderNo);

    ServerResponse<PageInfo> list(Integer userId, Integer current, Integer size);

    //manage
    ServerResponse<PageInfo> manageList(Integer current, Integer size);

    ServerResponse manageDetail(Long orderNo);

    ServerResponse<PageInfo> search(Long orderNo, Integer current, Integer size);

    ServerResponse<String> send(Long orderNo);

    //支付宝支付
    ServerResponse pay(Integer userId, Long orderNo, String path);

    ServerResponse aliPayCallBack(Map<String, String> params);

    ServerResponse queryOrderStatus(Integer userId, Long orderNo);

    //hour个小时后关闭订单
    void closeOrder(int hour);


}
