package com.mmall.controller.customer;

import com.mmall.common.Constants;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.CookieUtil;
import com.mmall.util.ShardedJedisPoolUtil;
import com.mmall.util.JsonUtil;
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
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private IUserService iUserService;

    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    /**
     *@param  [username, password, session]
     *@return com.mmall.common.ServerResponse<com.mmall.pojo.User>
     *@author 卢越
     *@date 2018/7/30
     */
    public ServerResponse<User> login(String username, String password, HttpSession session, HttpServletResponse servletResponse) {
        ServerResponse<User> response = iUserService.login(username, password);
        //校验用户名密码
        if (response.isSuccess()) {
            //Redis的key值存储在cookie中，这样每次判断用户是否登录只需要从cookie中获取key值，在从Redis取得user就行
            CookieUtil.writeCookie(session.getId(), servletResponse);
            //将登录用户的信息存储到Redis中
            ShardedJedisPoolUtil.setEx(session.getId(), JsonUtil.objToJson(response.getData()), Constants.RedisExTime.EX_TIME);
        }
        return response;
    }

    @RequestMapping(value = "logout.do", method = RequestMethod.POST)
    @ResponseBody
    /**
     *@param  [session]
     *@return com.mmall.common.ServerResponse<java.lang.String>
     *@author 卢越
     *@date 2018/7/30
     */
    public ServerResponse<String> logout(HttpServletRequest request, HttpServletResponse response) {
        String token = CookieUtil.readCookie(request);
        CookieUtil.delCookie(request, response);
        ShardedJedisPoolUtil.del(token);
//        session.removeAttribute(Constants.CURRENT_USER);
        return ServerResponse.createBySuccess();
    }

    @RequestMapping(value = "register.do", method = RequestMethod.POST)
    @ResponseBody
    /**
     *@param  [user]
     *@return com.mmall.common.ServerResponse<java.lang.String>
     *@author 卢越
     *@date 2018/7/30
     */
    public ServerResponse<String> register(User user) {
        return iUserService.register(user);
    }

    @RequestMapping(value = "check_valid.do", method = RequestMethod.POST)
    @ResponseBody
    /**校验用户名，邮箱
     *@param  [str, type]
     *@return com.mmall.common.ServerResponse<java.lang.String>
     *@author 卢越
     *@date 2018/7/30
     */
    public ServerResponse<String> checkValid(String str, String type) {
        return iUserService.checkValid(str, type);
    }

    @RequestMapping(value = "get_user_info.do", method = RequestMethod.POST)
    @ResponseBody
    /**获取用户信息
     *@param  [session]
     *@return com.mmall.common.ServerResponse<com.mmall.pojo.User>
     *@author 卢越
     *@date 2018/7/30
     */
    public ServerResponse<User> getUserInfo(HttpServletRequest request) {
        String token = CookieUtil.readCookie(request);
        User user = JsonUtil.json2Object(ShardedJedisPoolUtil.get(token), User.class);
        if (user == null) {
            return ServerResponse.createByErrorMessage("请登录");
        }
        return ServerResponse.createBySuccess(user);
    }

    @RequestMapping(value = "forget_get_question.do", method = RequestMethod.POST)
    @ResponseBody
    /**获取用户问题
     *@param  [username]
     *@return com.mmall.common.ServerResponse<java.lang.String>
     *@author 卢越
     *@date 2018/7/30
     */
    public ServerResponse<String> forgetGetQuestion(String username) {
        return iUserService.getForgetQuestion(username);
    }

    @RequestMapping(value = "forget_check_answer.do", method = RequestMethod.POST)
    @ResponseBody
    /**校验用户的答案是否正确, 返回一个token，用于forgetReSetPassword的密码更新
     *@param  [username, question, answer]
     *@return com.mmall.common.ServerResponse<java.lang.String>
     *@author 卢越
     *@date 2018/7/30
     */
    public ServerResponse<String> forgetCheckAnswer(String username, String question, String answer) {
        return iUserService.checkQuestionAnswer(username, question, answer);
    }


    @RequestMapping(value = "forget_reset_password.do", method = RequestMethod.POST)
    @ResponseBody
    /**忘记密码之更新密码
     *@param  [username, passwordNew, forgetToken]
     *@return com.mmall.common.ServerResponse<java.lang.String>
     *@author 卢越
     *@date 2018/7/30
     */
    public ServerResponse<String> forgetReSetPassword(String username, String passwordNew, String forgetToken) {
        return iUserService.forgetReSetPassword(username, passwordNew, forgetToken);
    }

    @RequestMapping(value = "reset_password.do", method = RequestMethod.POST)
    @ResponseBody
    /**登录状态更新密码
     *@param  [passwordOld, passwordNew, session]
     *@return com.mmall.common.ServerResponse<java.lang.String>
     *@author 卢越
     *@date 2018/7/30
     */
    public ServerResponse<String> resetPassword(String passwordOld, String passwordNew, HttpServletRequest request) {
        String token = CookieUtil.readCookie(request);
        User user = JsonUtil.json2Object(ShardedJedisPoolUtil.get(token), User.class);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }

        return iUserService.resetPassword(passwordOld, passwordNew, user);
    }

    @RequestMapping(value = "update_user.do", method = RequestMethod.POST)
    @ResponseBody
    /**更新用户信息
     *@param  [user, session]
     *@return com.mmall.common.ServerResponse<com.mmall.pojo.User>
     *@author 卢越
     *@date 2018/7/30
     */
    public ServerResponse<User> updateUser(User user, HttpServletRequest request) {
        String token = CookieUtil.readCookie(request);
        User currentUser = JsonUtil.json2Object(ShardedJedisPoolUtil.get(token), User.class);
        if (currentUser == null) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }

        user.setUsername(currentUser.getUsername());
        user.setId(currentUser.getId());
        user.setRole(currentUser.getRole());

        ServerResponse<User> response = iUserService.updateUser(user);
        if (response.isSuccess()) {
            ShardedJedisPoolUtil.set(token, JsonUtil.objToJson(response.getData()));
            return response;
        }
        return response;
    }

    @RequestMapping(value = "get_information.do", method = RequestMethod.POST)
    @ResponseBody
    /**获取用户信息， 用于二期分布式redis
     *@param  [session]
     *@return com.mmall.common.ServerResponse<com.mmall.pojo.User>
     *@author 卢越
     *@date 2018/7/30
     */
    public ServerResponse<User> getInformation(HttpServletRequest request) {
        String token = CookieUtil.readCookie(request);
        User currentUser = JsonUtil.json2Object(ShardedJedisPoolUtil.get(token), User.class);

        if (currentUser == null) {
            return ServerResponse.createByErrorCodeAndMessage(ResponseCode.NEED_LOGIN.getCode(), "用户需要登录 status=10");
        }

        return iUserService.getInformation(currentUser.getId());
    }
}
