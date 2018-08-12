package com.mmall.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Luyue
 * @date 2018/8/2 13:10
 **/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartVo {

    private List<CartProductVO> cartProductVOList;
    private BigDecimal cartTotalPrice;
    private Boolean allChecked;
    private String imgHost;

    public List<CartProductVO> getCartProductVOList() {
        return cartProductVOList;
    }

}
