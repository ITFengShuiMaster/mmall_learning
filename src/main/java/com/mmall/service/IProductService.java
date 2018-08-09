package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.vo.ProductDetailVO;

public interface IProductService {
    ServerResponse saveOrUpdateProduct(Product product);

    ServerResponse setSaleStatus(Integer productId, Integer status);

    ServerResponse<ProductDetailVO> getManagerProductDetail(Integer productId);

    ServerResponse<PageInfo> getManageProductList(int current, int size);

    ServerResponse<PageInfo> productSearch(String productName, Integer productId, int current, int size);

    ServerResponse<ProductDetailVO> getProductDetail(Integer productId);

    ServerResponse<PageInfo> getProductList(String productName, Integer categoryId, String orderBy, Integer current, Integer size);
}
