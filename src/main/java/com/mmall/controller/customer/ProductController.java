package com.mmall.controller.customer;

import com.mmall.common.ServerResponse;
import com.mmall.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Luyue
 * @date 2018/8/1 15:27
 **/
@Controller
@RequestMapping("/products/")
public class ProductController {
    private final IProductService iProductService;

    @Autowired
    public ProductController(IProductService iProductService) {
        this.iProductService = iProductService;
    }

    @RequestMapping(value = "detail.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getProductDetail(Integer productId) {
        return iProductService.getProductDetail(productId);
    }

    @RequestMapping(value = "list.do", method = RequestMethod.POST)
    @ResponseBody
    /**前台列表搜索，动态排序
     *@param  [productName, categoryId, orderBy, current, size]
     *@return com.mmall.common.ServerResponse
     *@author 卢越
     *@date 2018/8/1
     */
    public ServerResponse getProductList(@RequestParam(value = "keyWord", required = false) String productName,
                                         @RequestParam(value = "categoryId", required = false) Integer categoryId,
                                         @RequestParam(value = "orderBy", required = false) String orderBy,
                                         @RequestParam(value = "current", defaultValue = "1") Integer current,
                                         @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return iProductService.getProductList(productName, categoryId, orderBy, current, size);
    }
}
