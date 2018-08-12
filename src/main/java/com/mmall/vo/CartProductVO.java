package com.mmall.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 结合购物车和商品的VO
 *
 * @author Luyue
 * @date 2018/8/2 13:04
 **/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartProductVO {
    private Integer cartId;

    private Integer userId;

    private Integer productId;

    private Integer quantity;

    private String productName;
    private String productSubTitle;
    private String productMainImg;
    private BigDecimal productPrice;
    private Integer productStatus;
    private BigDecimal productTotalPrice;
    private Integer productStock;
    /**
     * 此商品在购物车中是否已勾选
     */
    private Integer productChecked;
    /**
     * 限制数量的一个返回结果
     */
    private String limitQuantity;
}
