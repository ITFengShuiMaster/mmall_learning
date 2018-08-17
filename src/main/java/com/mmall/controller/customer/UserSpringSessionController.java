package com.mmall.controller.customer;

import com.mmall.common.Constants;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.ShardedJedisPoolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Luyue
 * @date 2018/7/29 20:55
 **/
@Controller
@RequestMapping("/user/spring-session")
public class UserSpringSessionController {

    @Autowired
    private IUserService iUserService;

    @RequestMapping(value = "login.do", method = RequestMethod.GET)
    @ResponseBody
    /**
     *@param  [username, password, session]
     *@return com.mmall.common.ServerResponse<com.mmall.pojo.User>
     *@author 卢越
     *@date 2018/7/30
     */
    public ServerResponse<User> login(String username, String password, HttpSession session, HttpServletResponse servletResponse) {
        ServerResponse<User> response = iUserService.login(username, password);
        if (response.isSuccess()) {
            //Redis的key值存储在cookie中，这样每次判断用户是否登录只需要从cookie中获取key值，在从Redis取得user就行
//            CookieUtil.writeCookie(session.getId(), servletResponse);
//            //将登录用户的信息存储到Redis中
//            ShardedJedisPoolUtil.setEx(session.getId(), JsonUtil.objToJson(response.getData()), Constants.RedisExTime.EX_TIME);
            session.setAttribute(Constants.CURRENT_USER, response.getData());
        }
        return response;
    }

    @RequestMapping(value = "logout.do", method = RequestMethod.GET)
    @ResponseBody
    /**
     *@param  [session]
     *@return com.mmall.common.ServerResponse<java.lang.String>
     *@author 卢越
     *@date 2018/7/30
     */
    public ServerResponse<String> logout(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
//        String token = CookieUtil.readCookie(request);
//        CookieUtil.delCookie(request, response);
//        ShardedJedisPoolUtil.del(token);
        session.removeAttribute(Constants.CURRENT_USER);
        return ServerResponse.createBySuccess();
    }

    @RequestMapping(value = "get_user_info.do", method = RequestMethod.GET)
    @ResponseBody
    /**获取用户信息
     *@param  [session]
     *@return com.mmall.common.ServerResponse<com.mmall.pojo.User>
     *@author 卢越
     *@date 2018/7/30
     */
    public ServerResponse<User> getUserInfo(HttpSession session, HttpServletRequest request) {
//        String token = CookieUtil.readCookie(request);
//        User user = JsonUtil.json2Object(ShardedJedisPoolUtil.get(token), User.class);
        User user = (User) session.getAttribute(Constants.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("请登录");
        }
        return ServerResponse.createBySuccess(user);
    }
}
