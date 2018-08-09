package com.mmall.controller.manager;

import com.mmall.common.Constants;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IOrderService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @author Luyue
 * @date 2018/8/5 17:21
 **/
@Controller
@RequestMapping("/manage/order/")
public class OrderManagerController {

    private final IUserService iUserService;
    private final IOrderService iOrderService;

    @Autowired
    public OrderManagerController(IUserService iUserService, IOrderService iOrderService) {
        this.iUserService = iUserService;
        this.iOrderService = iOrderService;
    }

    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse list(@RequestParam(value = "current", defaultValue = "1") Integer current, @RequestParam(value = "size", defaultValue = "10") Integer size, HttpSession session) {
        User user = (User) session.getAttribute(Constants.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeAndMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录，请先登录");
        }

        if (iUserService.checkAdminValid(user).isSuccess()) {
            //业务
            return iOrderService.manageList(current, size);
        }

        return ServerResponse.createByErrorMessage("无权限操作");
    }

    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse detail(Long orderNo, HttpSession session) {
        User user = (User) session.getAttribute(Constants.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeAndMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录，请先登录");
        }

        if (iUserService.checkAdminValid(user).isSuccess()) {
            //业务
            return iOrderService.manageDetail(orderNo);
        }

        return ServerResponse.createByErrorMessage("无权限操作");
    }

    @RequestMapping("search.do")
    @ResponseBody
    public ServerResponse search(Long orderNo, @RequestParam(value = "current", defaultValue = "1") Integer current, @RequestParam(value = "size", defaultValue = "10") Integer size, HttpSession session) {
        User user = (User) session.getAttribute(Constants.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeAndMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录，请先登录");
        }

        if (iUserService.checkAdminValid(user).isSuccess()) {
            //业务
            return iOrderService.search(orderNo, current, size);
        }

        return ServerResponse.createByErrorMessage("无权限操作");
    }

    @RequestMapping("sendOrder.do")
    @ResponseBody
    public ServerResponse sendOrder(Long orderNo, HttpSession session) {
        User user = (User) session.getAttribute(Constants.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeAndMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录，请先登录");
        }

        if (iUserService.checkAdminValid(user).isSuccess()) {
            //业务
            return iOrderService.send(orderNo);
        }

        return ServerResponse.createByErrorMessage("无权限操作");
    }
}
