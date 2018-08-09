package com.mmall.service.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.mmall.common.Constants;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CartMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Cart;
import com.mmall.pojo.Product;
import com.mmall.service.ICartService;
import com.mmall.util.BigDecimalUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.CartProductVO;
import com.mmall.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Luyue
 * @date 2018/7/29 22:06
 **/
@Service("iCartService")
public class CartServiceImpl implements ICartService {
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;

    public CartVo getCartVOLimit(Integer userId) {
        CartVo cartVo = new CartVo();
        List<Cart> cartList = cartMapper.selectListByUserId(userId);
        List<CartProductVO> cartProductVOList = Lists.newArrayList();

        //购物车整个总价
        BigDecimal totalPrice = new BigDecimal("0");

        if (cartList.size() != 0) {
            for (Cart cartItem : cartList) {
                CartProductVO cartProductVO = new CartProductVO();
                cartProductVO.setCartId(cartItem.getId());
                cartProductVO.setUserId(cartItem.getUserId());

                Product product = productMapper.selectByPrimaryKey(cartItem.getProductId());
                if (product != null) {
                    cartProductVO.setProductId(product.getId());
                    cartProductVO.setProductMainImg(product.getMainImage());
                    cartProductVO.setProductName(product.getName());
                    cartProductVO.setProductPrice(product.getPrice());
                    cartProductVO.setProductStatus(product.getStatus());
                    cartProductVO.setProductStock(product.getStock());
                    cartProductVO.setProductSubTitle(product.getSubtitle());

                    int realStocks = 0;
                    if (product.getStock() >= cartItem.getQuantity()) {
                        realStocks = cartItem.getQuantity();
                        cartProductVO.setLimitQuantity(Constants.Cart.LIMIT_QUANTITY_SUCCESS);
                    } else {
                        realStocks = product.getStock();
                        cartProductVO.setLimitQuantity(Constants.Cart.LIMIT_QUANTITY_FAIL);
                        //更新购物车中商品有效数量
                        Cart cartUpdateQuantity = new Cart();
                        cartUpdateQuantity.setId(cartItem.getId());
                        cartUpdateQuantity.setQuantity(product.getStock());
                        cartMapper.updateByPrimaryKeySelective(cartUpdateQuantity);
                    }

                    cartProductVO.setQuantity(realStocks);
                    //计算商品总价
                    cartProductVO.setProductTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(), cartItem.getQuantity()));
                    cartProductVO.setProductChecked(cartItem.getChecked());
                }

                if (cartItem.getChecked() == Constants.Cart.CHECKED) {
                    totalPrice = BigDecimalUtil.add(totalPrice.doubleValue(), cartProductVO.getProductTotalPrice().doubleValue());
                }
                cartProductVOList.add(cartProductVO);
            }
        }

        cartVo.setCartTotalPrice(totalPrice);
        cartVo.setCartProductVOList(cartProductVOList);
        cartVo.setImgHost(PropertiesUtil.getKey("ftp.server.http.prefix"));
        cartVo.setAllChecked(isAllChecked(userId));

        return cartVo;
    }

    private boolean isAllChecked(Integer userId) {
        if (userId == null) {
            return false;
        }

        return cartMapper.selectCartCheckedByUserId(userId) == 0;
    }

    @Override
    public ServerResponse<CartVo> addCart(Integer userId, Integer productId, Integer count) {
        if (productId == null || count == null) {
            return ServerResponse.createByErrorCodeAndMessage(ResponseCode.ILLEGAL_PARM.getCode(), ResponseCode.ILLEGAL_PARM.getDesc());
        }

        Cart cart = cartMapper.selectByUserIdAndProductId(userId, productId);
        if (cart == null) {
            cart = new Cart();
            cart.setProductId(productId);
            cart.setQuantity(count);
            cart.setUserId(userId);
            cart.setChecked(Constants.Cart.CHECKED);
            cartMapper.insert(cart);
        } else {
            //产品已存在，数量增加
            cart.setQuantity(cart.getQuantity() + count);
            cartMapper.updateByPrimaryKeySelective(cart);
        }

        CartVo cartVo = getCartVOLimit(userId);
        return ServerResponse.createBySuccess(cartVo);
    }

    @Override
    public ServerResponse<CartVo> updateCart(Integer userId, Integer productId, Integer count) {
        if (productId == null || count == null) {
            return ServerResponse.createByErrorCodeAndMessage(ResponseCode.ILLEGAL_PARM.getCode(), ResponseCode.ILLEGAL_PARM.getDesc());
        }

        Cart cart = cartMapper.selectByUserIdAndProductId(userId, productId);
        if (cart != null) {
            cart.setQuantity(count);
            cartMapper.updateByPrimaryKeySelective(cart);
        } else {
            return ServerResponse.createBySuccessMessage("购物车中没有该商品");
        }

        CartVo cartVo = getCartVOLimit(userId);
        return ServerResponse.createBySuccess(cartVo);
    }

    @Override
    public ServerResponse<CartVo> deleteCart(Integer userId, String productIds) {
        List<String> productIdList = Splitter.on(",").splitToList(productIds);

        if (productIdList.size() == 0) {
            return ServerResponse.createByErrorCodeAndMessage(ResponseCode.ILLEGAL_PARM.getCode(), ResponseCode.ILLEGAL_PARM.getDesc());
        }

        cartMapper.deleteByUserIdAndProductIds(userId, productIdList);

        CartVo cartVo = getCartVOLimit(userId);
        return ServerResponse.createBySuccess(cartVo);
    }

    @Override
    public ServerResponse<CartVo> list(Integer userId) {
        CartVo cartVo = getCartVOLimit(userId);
        return ServerResponse.createBySuccess(cartVo);
    }

    @Override
    public ServerResponse<CartVo> selectOrUnSelectChecked(Integer userId, Integer productId, Integer checked) {
        cartMapper.checkedUnCheckedProduct(userId, productId, checked);
        return list(userId);
    }

    @Override
    public ServerResponse<Integer> getCartCount(Integer userId) {
        return ServerResponse.createBySuccess(cartMapper.selectSumCart(userId));
    }
}
