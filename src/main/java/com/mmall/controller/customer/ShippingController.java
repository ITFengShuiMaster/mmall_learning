package com.mmall.controller.customer;

import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Shipping;
import com.mmall.pojo.User;
import com.mmall.service.IShippingService;
import com.mmall.util.CookieUtil;
import com.mmall.util.ShardedJedisPoolUtil;
import com.mmall.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Luyue
 * @date 2018/8/3 13:49
 **/
@Controller
@RequestMapping("/ships/")
public class ShippingController {

    private final IShippingService iShippingService;

    @Autowired
    public ShippingController(IShippingService iShippingService) {
        this.iShippingService = iShippingService;
    }

    @RequestMapping(value = "add.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse add(Shipping shipping, HttpServletRequest request) {
        String token = CookieUtil.readCookie(request);
        User user = JsonUtil.json2Object(ShardedJedisPoolUtil.get(token), User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeAndMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return iShippingService.add(user.getId(), shipping);
    }

    @RequestMapping(value = "del.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse del(Integer shippingId, HttpServletRequest request) {
        String token = CookieUtil.readCookie(request);
        User user = JsonUtil.json2Object(ShardedJedisPoolUtil.get(token), User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeAndMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return iShippingService.del(user.getId(), shippingId);
    }

    @RequestMapping(value = "update.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse update(Shipping shipping, HttpServletRequest request) {
        String token = CookieUtil.readCookie(request);
        User user = JsonUtil.json2Object(ShardedJedisPoolUtil.get(token), User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeAndMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return iShippingService.update(user.getId(), shipping);
    }

    @RequestMapping(value = "select.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse select(Integer shippingId, HttpServletRequest request) {
        String token = CookieUtil.readCookie(request);
        User user = JsonUtil.json2Object(ShardedJedisPoolUtil.get(token), User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeAndMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return iShippingService.select(user.getId(), shippingId);
    }

    @RequestMapping(value = "list.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse list(@RequestParam(value = "current", defaultValue = "1") Integer current,
                               @RequestParam(value = "size", defaultValue = "10") Integer size, HttpServletRequest request) {
        String token = CookieUtil.readCookie(request);
        User user = JsonUtil.json2Object(ShardedJedisPoolUtil.get(token), User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeAndMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return iShippingService.list(user.getId(), current, size);
    }
}