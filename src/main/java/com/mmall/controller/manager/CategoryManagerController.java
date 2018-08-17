package com.mmall.controller.manager;

import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
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
 * @date 2018/7/31 10:44
 **/
@Controller
@RequestMapping("/manage/category/")
public class CategoryManagerController {

    private final IUserService iUserService;
    private final ICategoryService iCategoryService;

    @Autowired
    public CategoryManagerController(IUserService iUserService, ICategoryService iCategoryService) {
        this.iUserService = iUserService;
        this.iCategoryService = iCategoryService;
    }

    @RequestMapping(value = "add_category.do", method = RequestMethod.POST)
    @ResponseBody
    /**添加商品分类
     *@param  [categoryName, parentId, session]
     *@return com.mmall.common.ServerResponse
     *@author 卢越
     *@date 2018/7/31
     */
    public ServerResponse addCategory(String categoryName, @RequestParam(value = "parentId", defaultValue = "0") Integer parentId, HttpServletRequest request) {
//        String token = CookieUtil.readCookie(request);
//        User user = JsonUtil.json2Object(ShardedJedisPoolUtil.get(token), User.class);
//        if (user == null) {
//            return ServerResponse.createByErrorCodeAndMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录，请先登录");
//        }
//
//        if (iUserService.checkAdminValid(user).isSuccess()) {
//            //添加商品分类
//            return iCategoryService.addCategory(categoryName, parentId);
//        }
//
//        return ServerResponse.createByErrorMessage("无权限操作");
        //改造成通过拦截器验证用户是否登录并权限判断
        return iCategoryService.addCategory(categoryName, parentId);
    }

    @RequestMapping(value = "update_category_name.do", method = RequestMethod.POST)
    @ResponseBody
    /**更新商品分类名称
     *@param  [categoryName, categoryId, session]
     *@return com.mmall.common.ServerResponse
     *@author 卢越
     *@date 2018/7/31
     */
    public ServerResponse updateCategoryName(String categoryName, Integer categoryId, HttpServletRequest request) {
//        String token = CookieUtil.readCookie(request);
//        User user = JsonUtil.json2Object(ShardedJedisPoolUtil.get(token), User.class);
//        if (user == null) {
//            return ServerResponse.createByErrorCodeAndMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录，请先登录");
//        }
//
//        if (iUserService.checkAdminValid(user).isSuccess()) {
//            //更新商品名称
//            return iCategoryService.updateCategoryName(categoryName, categoryId);
//        }
//
//        return ServerResponse.createByErrorMessage("无权限操作");
        //改造成通过拦截器验证用户是否登录并权限判断
        return iCategoryService.updateCategoryName(categoryName, categoryId);
    }

    @RequestMapping(value = "get_category.do", method = RequestMethod.POST)
    @ResponseBody
    /**获取子节点平级category
     *@param  [categoryId, session]
     *@return com.mmall.common.ServerResponse
     *@author 卢越
     *@date 2018/8/1
     */
    public ServerResponse getParallelCategory(Integer categoryId, HttpServletRequest request) {
//        String token = CookieUtil.readCookie(request);
//        User user = JsonUtil.json2Object(ShardedJedisPoolUtil.get(token), User.class);
//        if (user == null) {
//            return ServerResponse.createByErrorCodeAndMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录，请先登录");
//        }
//
//        if (iUserService.checkAdminValid(user).isSuccess()) {
//            //获取子节点平级category
//            return iCategoryService.getParallelCategory(categoryId);
//        }
//
//        return ServerResponse.createByErrorMessage("无权限操作");
        //改造成通过拦截器验证用户是否登录并权限判断
        return iCategoryService.getParallelCategory(categoryId);
    }

    @RequestMapping(value = "get_deep_category.do", method = RequestMethod.POST)
    @ResponseBody
    /**递归获取子节点
     *@param  [categoryId, session]
     *@return com.mmall.common.ServerResponse
     *@author 卢越
     *@date 2018/8/1
     */
    public ServerResponse getDeeplCategory(Integer categoryId, HttpServletRequest request) {
//        String token = CookieUtil.readCookie(request);
//        User user = JsonUtil.json2Object(ShardedJedisPoolUtil.get(token), User.class);
//        if (user == null) {
//            return ServerResponse.createByErrorCodeAndMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录，请先登录");
//        }
//
//        if (iUserService.checkAdminValid(user).isSuccess()) {
//            //递归获取子节点
//            return iCategoryService.getDeepCategory(categoryId);
//        }
//
//        return ServerResponse.createByErrorMessage("无权限操作");
        //改造成通过拦截器验证用户是否登录并权限判断
        return iCategoryService.getDeepCategory(categoryId);
    }
}
