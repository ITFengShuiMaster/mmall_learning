package com.mmall.controller.customer;

import com.mmall.common.Constants;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICartService;
import com.mmall.util.CookieUtil;
import com.mmall.util.ShardedJedisPoolUtil;
import com.mmall.util.JsonUtil;
import com.mmall.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/** 购物车接口
 * @author Luyue
 * @date 2018/8/2 12:52
 **/
@Controller
@RequestMapping("/carts/")
public class CartController {

    private final ICartService iCartService;

    @Autowired
    public CartController(ICartService iCartService) {
        this.iCartService = iCartService;
    }

    @RequestMapping(value = "list.do", method = RequestMethod.GET)
    @ResponseBody
    /** 返回购物车中的商品信息，和总价
     *@param  [request]
     *@return  com.mmall.common.ServerResponse<com.mmall.vo.CartVo>
     *@author  卢越
     *@date  2018/8/19
     */
    public ServerResponse<CartVo> list(HttpServletRequest request) {
        String token = CookieUtil.readCookie(request);
        User user = JsonUtil.json2Object(ShardedJedisPoolUtil.get(token), User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeAndMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return iCartService.list(user.getId());
    }

    @RequestMapping(value = "add.do", method = RequestMethod.POST)
    @ResponseBody
    /** 添加商品到购物车
     *@param  [productId, count, request]
     *@return  com.mmall.common.ServerResponse<com.mmall.vo.CartVo>
     *@author  卢越
     *@date  2018/8/19
     */
    public ServerResponse<CartVo> add(Integer productId, Integer count, HttpServletRequest request) {
        String token = CookieUtil.readCookie(request);
        User user = JsonUtil.json2Object(ShardedJedisPoolUtil.get(token), User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeAndMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return iCartService.addCart(user.getId(), productId, count);
    }

    @RequestMapping(value = "update.do", method = RequestMethod.POST)
    @ResponseBody
    /** 更新购物车中商品的数量
     *@param  [productId, count, request]
     *@return  com.mmall.common.ServerResponse<com.mmall.vo.CartVo>
     *@author  卢越
     *@date  2018/8/19
     */
    public ServerResponse<CartVo> update(Integer productId, Integer count, HttpServletRequest request) {
        String token = CookieUtil.readCookie(request);
        User user = JsonUtil.json2Object(ShardedJedisPoolUtil.get(token), User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeAndMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return iCartService.updateCart(user.getId(), productId, count);
    }

    @RequestMapping(value = "delete.do", method = RequestMethod.POST)
    @ResponseBody
    /** 删除购物车中商品
     *@param  [productIds, request]
     *@return  com.mmall.common.ServerResponse<com.mmall.vo.CartVo>
     *@author  卢越
     *@date  2018/8/19
     */
    public ServerResponse<CartVo> delete(String productIds, HttpServletRequest request) {
        String token = CookieUtil.readCookie(request);
        User user = JsonUtil.json2Object(ShardedJedisPoolUtil.get(token), User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeAndMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return iCartService.deleteCart(user.getId(), productIds);
    }

    @RequestMapping(value = "select_all.do", method = RequestMethod.POST)
    @ResponseBody
    /** 全选购物车中商品
     *@param  [request]
     *@return  com.mmall.common.ServerResponse<com.mmall.vo.CartVo>
     *@author  卢越
     *@date  2018/8/19
     */
    public ServerResponse<CartVo> selectAll(HttpServletRequest request) {
        String token = CookieUtil.readCookie(request);
        User user = JsonUtil.json2Object(ShardedJedisPoolUtil.get(token), User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeAndMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return iCartService.selectOrUnSelectChecked(user.getId(), null, Constants.Cart.CHECKED);
    }

    @RequestMapping(value = "unselect_all.do", method = RequestMethod.POST)
    @ResponseBody
    /** 取消全选
     *@param  [request]
     *@return  com.mmall.common.ServerResponse<com.mmall.vo.CartVo>
     *@author  卢越
     *@date  2018/8/19
     */
    public ServerResponse<CartVo> unSelectAll(HttpServletRequest request) {
        String token = CookieUtil.readCookie(request);
        User user = JsonUtil.json2Object(ShardedJedisPoolUtil.get(token), User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeAndMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return iCartService.selectOrUnSelectChecked(user.getId(), null, Constants.Cart.UN_CHECKED);
    }

    @RequestMapping(value = "select.do", method = RequestMethod.POST)
    @ResponseBody
    /** 单选
     *@param  [productId, request]
     *@return  com.mmall.common.ServerResponse<com.mmall.vo.CartVo>
     *@author  卢越
     *@date  2018/8/19
     */
    public ServerResponse<CartVo> select(Integer productId, HttpServletRequest request) {
        String token = CookieUtil.readCookie(request);
        User user = JsonUtil.json2Object(ShardedJedisPoolUtil.get(token), User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeAndMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return iCartService.selectOrUnSelectChecked(user.getId(), productId, Constants.Cart.CHECKED);
    }

    @RequestMapping(value = "unSelect.do", method = RequestMethod.POST)
    @ResponseBody
    /** 取消单选择
     *@param  [productId, request]
     *@return  com.mmall.common.ServerResponse<com.mmall.vo.CartVo>
     *@author  卢越
     *@date  2018/8/19
     */
    public ServerResponse<CartVo> unSelect(Integer productId, HttpServletRequest request) {
        String token = CookieUtil.readCookie(request);
        User user = JsonUtil.json2Object(ShardedJedisPoolUtil.get(token), User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeAndMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return iCartService.selectOrUnSelectChecked(user.getId(), productId, Constants.Cart.UN_CHECKED);
    }

    @RequestMapping(value = "get_cart_count.do", method = RequestMethod.POST)
    @ResponseBody
    /** 返回购物车中商品
     *@param  [request]
     *@return  com.mmall.common.ServerResponse<java.lang.Integer>
     *@author  卢越
     *@date  2018/8/19
     */
    public ServerResponse<Integer> getCartCount(HttpServletRequest request) {
        String token = CookieUtil.readCookie(request);
        User user = JsonUtil.json2Object(ShardedJedisPoolUtil.get(token), User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeAndMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.getCartCount(user.getId());
    }
}
