package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.mmall.common.Constants;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Category;
import com.mmall.pojo.Product;
import com.mmall.service.ICategoryService;
import com.mmall.service.IProductService;
import com.mmall.util.DateTimeUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.ProductDetailVO;
import com.mmall.vo.ProductListVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Luyue
 * @date 2018/7/31 15:43
 **/
@Service("iProductService")
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private ICategoryService iCategoryService;

    private ProductDetailVO initProductDetailVO(Product product) {
        ProductDetailVO productDetailVO = new ProductDetailVO();
        productDetailVO.setCategoryId(product.getCategoryId());
        productDetailVO.setDetail(product.getDetail());
        productDetailVO.setId(product.getId());
        productDetailVO.setMainImage(product.getMainImage());
        productDetailVO.setName(product.getName());
        productDetailVO.setPrice(product.getPrice());
        productDetailVO.setStatus(product.getStatus());
        productDetailVO.setStock(product.getStock());
        productDetailVO.setSubImages(product.getSubImages());
        productDetailVO.setSubtitle(product.getSubtitle());

        productDetailVO.setImageHost(PropertiesUtil.getKey("ftp.server.http.prefix", "ftp:118.24.116.137/img/"));

        Category category = categoryMapper.selectByPrimaryKey(productDetailVO.getCategoryId());
        if (category == null) {
            productDetailVO.setParentCategoryId(0);
        } else {
            productDetailVO.setParentCategoryId(category.getParentId());
        }

        productDetailVO.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
        productDetailVO.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));

        return productDetailVO;
    }

    private ProductListVO initProductListVO(Product product) {
        ProductListVO productListVO = new ProductListVO();
        productListVO.setCategoryId(product.getCategoryId());
        productListVO.setId(product.getId());
        productListVO.setMainImage(product.getMainImage());
        productListVO.setName(product.getName());
        productListVO.setPrice(product.getPrice());
        productListVO.setStatus(product.getStatus());
        productListVO.setSubtitle(product.getSubtitle());

        productListVO.setImgHost(PropertiesUtil.getKey("ftp.server.http.prefix", "ftp:118.24.116.137/img/"));

        return productListVO;
    }

    @Override
    public ServerResponse saveOrUpdateProduct(Product product) {
        if (product == null) {
            return ServerResponse.createByErrorMessage("参数不正确");
        }

        if (StringUtils.isNotBlank(product.getSubImages())) {
            String[] subImages = product.getSubImages().split(",");
            if (subImages.length > 0) {
                product.setMainImage(subImages[0]);
            }
        }

        if (product.getId() != null) {
            //新增产品
            if (productMapper.insert(product) > 0) {
                return ServerResponse.createBySuccessMessage("新增产品成功");
            } else {
                return ServerResponse.createByErrorMessage("新增产品失败");
            }
        } else {
            //更新产品
            if (productMapper.updateByPrimaryKey(product) > 0) {
                return ServerResponse.createBySuccessMessage("更新产品成功");
            } else {
                return ServerResponse.createByErrorMessage("更新产品失败");
            }
        }
    }

    @Override
    public ServerResponse setSaleStatus(Integer productId, Integer status) {
        if (productId == null || status == null) {
            return ServerResponse.createByErrorCodeAndMessage(ResponseCode.ILLEGAL_PARM.getCode(), ResponseCode.ILLEGAL_PARM.getDesc());
        }

        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);

        if (productMapper.updateByPrimaryKeySelective(product) > 0) {
            return ServerResponse.createBySuccessMessage("更新商品状态成功");
        }

        return ServerResponse.createByErrorMessage("更新商品状态失败");
    }

    @Override
    public ServerResponse<ProductDetailVO> getManagerProductDetail(Integer productId) {
        if (productId == null) {
            return ServerResponse.createByErrorCodeAndMessage(ResponseCode.ILLEGAL_PARM.getCode(), ResponseCode.ILLEGAL_PARM.getDesc());
        }

        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return ServerResponse.createByErrorMessage("产品已下架或删除");
        }

        ProductDetailVO productDetailVO = initProductDetailVO(product);
        return ServerResponse.createBySuccess(productDetailVO);

    }

    @Override
    public ServerResponse<PageInfo> getManageProductList(int current, int size) {
        //mybatis-pageHelper 分页步骤
        //1. startPage
        PageHelper.startPage(current, size);
        //2. 填充sql逻辑
        List<Product> productList = productMapper.selectList();
        List<ProductListVO> productListVOS = Lists.newArrayList();
        for (Product product : productList) {
            productListVOS.add(initProductListVO(product));
        }
        //3. PageHelper 收尾
        PageInfo pageResult = new PageInfo(productList);
        pageResult.setList(productListVOS);

        return ServerResponse.createBySuccess(pageResult);
    }

    @Override
    public ServerResponse<PageInfo> productSearch(String productName, Integer productId, int current, int size) {
        //1. startPage
        PageHelper.startPage(current, size);

        if (StringUtils.isNotBlank(productName)) {
            productName = new StringBuilder().append("%").append(productName).append("%").toString();
        }
        //2. 填充sql逻辑
        List<Product> productList = productMapper.selectListByProductNameAndProductId(productName, productId);
        List<ProductListVO> productListVOS = Lists.newArrayList();
        for (Product product : productList) {
            productListVOS.add(initProductListVO(product));
        }
        //3. PageHelper 收尾
        PageInfo pageResult = new PageInfo(productList);
        pageResult.setList(productListVOS);

        return ServerResponse.createBySuccess(pageResult);
    }

    @Override
    public ServerResponse<ProductDetailVO> getProductDetail(Integer productId) {
        if (productId == null) {
            return ServerResponse.createByErrorCodeAndMessage(ResponseCode.ILLEGAL_PARM.getCode(), ResponseCode.ILLEGAL_PARM.getDesc());
        }

        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return ServerResponse.createByErrorMessage("产品已下架或删除");
        }

        if (!product.getStatus().equals(Constants.SALE_STATUS.STATUS_ONLINE.getStatus())) {
            return ServerResponse.createByErrorMessage("产品已下架或删除");
        }

        return ServerResponse.createBySuccess(this.initProductDetailVO(product));
    }

    @Override
    public ServerResponse<PageInfo> getProductList(String productName, Integer categoryId, String orderBy, Integer current, Integer size) {
        if (StringUtils.isBlank(productName) && categoryId == null) {
            return ServerResponse.createByErrorCodeAndMessage(ResponseCode.ILLEGAL_PARM.getCode(), ResponseCode.ILLEGAL_PARM.getDesc());
        }

        List<Integer> categoryIds = Lists.newArrayList();
        if (categoryId != null) {
            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            if (category == null && StringUtils.isBlank(productName)) {
                PageHelper.startPage(current, size);
                List<ProductListVO> productListVOList = Lists.newArrayList();
                return ServerResponse.createBySuccess(new PageInfo<ProductListVO>(productListVOList));
            }
            categoryIds = iCategoryService.getDeepCategory(category.getId()).getData();
        }

        if (StringUtils.isNotBlank(productName)) {
            productName = new StringBuilder().append("%").append(productName).append("%").toString();
        }

        PageHelper.startPage(current, size);
        //排序处理
        if (StringUtils.isNotBlank(orderBy)) {
            if (Constants.ProductListOrderBy.sets.contains(orderBy)) {
                PageHelper.orderBy(orderBy.replace("_", " "));
            }
        }

        List<Product> productList = productMapper.selectListByProductNameAndCategoryId(StringUtils.isNotBlank(productName) ? productName : null, categoryIds.size() == 0 ? null : categoryIds);
        List<ProductListVO> productListVOList = Lists.newArrayList();
        for (Product product : productList) {
            productListVOList.add(initProductListVO(product));
        }
        PageInfo pageResult = new PageInfo(productList);
        pageResult.setList(productListVOList);

        return ServerResponse.createBySuccess(pageResult);
    }
}
