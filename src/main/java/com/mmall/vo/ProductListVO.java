package com.mmall.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author Luyue
 * @date 2018/8/1 10:54
 **/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductListVO {

    private Integer id;

    private Integer categoryId;

    private String name;

    private String subtitle;

    private String mainImage;

    private BigDecimal price;

    private Integer status;

    private String imgHost;

}
