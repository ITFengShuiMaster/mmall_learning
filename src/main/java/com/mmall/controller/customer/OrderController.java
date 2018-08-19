package com.mmall.controller.customer;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.mmall.common.Constants;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IOrderService;
import com.mmall.util.CookieUtil;
import com.mmall.util.ShardedJedisPoolUtil;
import com.mmall.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Luyue
 * @date 2018/8/4 14:40
 **/
@Controller
@RequestMapping("/order/")
public class OrderController {
    private static Logger logger = LoggerFactory.getLogger(OrderController.class);

    private final IOrderService iOrderService;

    @Autowired
    public OrderController(IOrderService iOrderService) {
        this.iOrderService = iOrderService;
    }

    @RequestMapping("create.do")
    @ResponseBody
    /**创建订单
     *@param  [shippingId, session]
     *@return com.mmall.common.ServerResponse
     *@author 卢越
     *@date 2018/8/5
     */
    public ServerResponse create(Integer shippingId, HttpServletRequest request) {
        String token = CookieUtil.readCookie(request);
        User user = JsonUtil.json2Object(ShardedJedisPoolUtil.get(token), User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeAndMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return iOrderService.create(user.getId(), shippingId);
    }

    @RequestMapping("cancel.do")
    @ResponseBody
    /**取消订单
     *@param  [orderNo, session]
     *@return com.mmall.common.ServerResponse
     *@author 卢越
     *@date 2018/8/5
     */
    public ServerResponse cancel(Long orderNo, HttpServletRequest request) {
        String token = CookieUtil.readCookie(request);
        User user = JsonUtil.json2Object(ShardedJedisPoolUtil.get(token), User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeAndMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return iOrderService.cancel(user.getId(), orderNo);
    }

    @RequestMapping("get_cart_product.do")
    @ResponseBody
    /**获取购物车物品
     *@param  [session]
     *@return com.mmall.common.ServerResponse
     *@author 卢越
     *@date 2018/8/5
     */
    public ServerResponse getCartProduct(HttpServletRequest request) {
        String token = CookieUtil.readCookie(request);
        User user = JsonUtil.json2Object(ShardedJedisPoolUtil.get(token), User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeAndMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return iOrderService.getCartProduct(user.getId());
    }

    @RequestMapping("detail.do")
    @ResponseBody
    /**订单详情
     *@param  [orderNo, session]
     *@return com.mmall.common.ServerResponse
     *@author 卢越
     *@date 2018/8/5
     */
    public ServerResponse detail(Long orderNo, HttpServletRequest request) {
        String token = CookieUtil.readCookie(request);
        User user = JsonUtil.json2Object(ShardedJedisPoolUtil.get(token), User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeAndMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return iOrderService.detail(user.getId(), orderNo);
    }

    @RequestMapping("list.do")
    @ResponseBody
    /** 订单列表分页
     *@param  [current, size, request]
     *@return  com.mmall.common.ServerResponse<com.github.pagehelper.PageInfo>
     *@author  卢越
     *@date  2018/8/19
     */
    public ServerResponse<PageInfo> list(@RequestParam(value = "current", defaultValue = "1") Integer current, @RequestParam(value = "size", defaultValue = "10") Integer size, HttpServletRequest request) {
        String token = CookieUtil.readCookie(request);
        User user = JsonUtil.json2Object(ShardedJedisPoolUtil.get(token), User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeAndMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return iOrderService.list(user.getId(), current, size);
    }


    @RequestMapping(value = "pay.do", method = RequestMethod.POST)
    @ResponseBody
    /** 支付模块
     *@param  [orderNo, request]
     *@return  com.mmall.common.ServerResponse
     *@author  卢越
     *@date  2018/8/19
     */
    public ServerResponse pay(Long orderNo, HttpServletRequest request) {
        String token = CookieUtil.readCookie(request);
        User user = JsonUtil.json2Object(ShardedJedisPoolUtil.get(token), User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeAndMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        String path = request.getSession().getServletContext().getRealPath("upload");
        return iOrderService.pay(user.getId(), orderNo, path);
    }

    @RequestMapping(value = "ali_pay_call_back.do", method = RequestMethod.POST)
    @ResponseBody
    /** 支付宝支付回调
     *@param  [request]
     *@return  java.lang.Object
     *@author  卢越
     *@date  2018/8/19
     */
    public Object alipayCallBack(HttpServletRequest request) {
        Map<String, String> params = Maps.newHashMap();

        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }

        logger.info("支付宝回调， sign: {} , trade_status: {} , 参数: {}", params.get("sign"), params.get("trade_status"), params);
        params.remove("sign_type");

        try {
            boolean rsaAliCheckedV2 = AlipaySignature.rsaCheckV2(params, Configs.getAlipayPublicKey(), "utf-8", Configs.getSignType());

            if (!rsaAliCheckedV2) {
                return ServerResponse.createByErrorMessage("非法请求");
            }
        } catch (AlipayApiException e) {
            logger.error("支付宝回调异常", e);
        }

        // TODO 验证各种数据的正确性
        ServerResponse response = iOrderService.aliPayCallBack(params);
        if (response.isSuccess()) {
            return Constants.AliPayCallBack.RESPONSE_SUCCESS;
        }

        return Constants.AliPayCallBack.RESPONSE_FAILED;
    }

    @RequestMapping(value = "query_order_pay_status.do", method = RequestMethod.POST)
    @ResponseBody
    /** 返回是否支付成功
     *@param  [orderNo, request]
     *@return  com.mmall.common.ServerResponse<java.lang.Boolean>
     *@author  卢越
     *@date  2018/8/19
     */
    public ServerResponse<Boolean> queryOrderPayStatus(Long orderNo, HttpServletRequest request) {
        String token = CookieUtil.readCookie(request);
        User user = JsonUtil.json2Object(ShardedJedisPoolUtil.get(token), User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeAndMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return iOrderService.queryOrderStatus(user.getId(), orderNo).isSuccess() ? ServerResponse.createBySuccess(true) : ServerResponse.createBySuccess(false);
    }
}
