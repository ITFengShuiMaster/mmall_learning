package com.mmall.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author Luyue
 * @date 2018/7/31 16:45
 **/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailVO {
    private Integer id;

    private Integer categoryId;

    private String name;

    private String subtitle;

    private String mainImage;

    private String subImages;

    private String detail;

    private BigDecimal price;

    private Integer stock;

    private Integer status;

    /**
     * 图片服务器地址
     */
    private String imageHost;

    private Integer parentCategoryId;

    private String createTime;

    private String updateTime;

}
