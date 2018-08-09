package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.vo.CartVo;

public interface ICartService {

    ServerResponse<CartVo> addCart(Integer userId, Integer productId, Integer count);

    ServerResponse<CartVo> updateCart(Integer userId, Integer productId, Integer count);

    ServerResponse<CartVo> deleteCart(Integer userId, String productIds);

    ServerResponse<CartVo> list(Integer userId);

    ServerResponse<CartVo> selectOrUnSelectChecked(Integer userId, Integer productId, Integer checked);

    ServerResponse<Integer> getCartCount(Integer userId);
}
