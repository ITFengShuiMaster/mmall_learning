package com.mmall.dao;

import com.mmall.pojo.Cart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    Cart selectByUserIdAndProductId(@Param("userId") Integer userId, @Param("productId") Integer productId);

    List<Cart> selectListByUserId(Integer userId);

    int selectCartCheckedByUserId(Integer userId);

    int deleteByUserIdAndProductIds(@Param("userId") Integer userId, @Param("productIds") List<String> productIds);

    int checkedUnCheckedProduct(@Param("userId") Integer userId, @Param("productId") Integer productId, @Param("checked") Integer checked);

    int selectSumCart(Integer userId);

    List<Cart> selectCheckedCartList(Integer userId);
}