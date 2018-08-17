package com.mmall.controller.manager;

import com.mmall.common.Constants;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @author Luyue
 * @date 2018/7/30 16:37
 **/
@Controller
@RequestMapping("/manage/user/")
public class UserManagerController {

    @Autowired
    private IUserService iUserService;

    @RequestMapping(value = "login.do", method = RequestMethod.GET)
    @ResponseBody
    /**管理员登录
     *@param  [username, password, session]
     *@return com.mmall.common.ServerResponse<com.mmall.pojo.User>
     *@author 卢越
     *@date 2018/7/30
     */
    public ServerResponse<User> login(String username, String password, HttpSession session) {
        ServerResponse<User> response = iUserService.login(username, password);
        if (response.isSuccess()) {
            if (response.getData().getRole() == Constants.ROLE.ROLE_ADMIN) {
                session.setAttribute(Constants.CURRENT_USER, response.getData());
                return response;
            } else {
                return ServerResponse.createByErrorMessage("用户权限不足");
            }
        }
        return response;
    }
}
