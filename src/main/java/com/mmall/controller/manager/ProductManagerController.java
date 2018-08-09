package com.mmall.controller.manager;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.mmall.common.Constants;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IFileService;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import com.mmall.util.PropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author Luyue
 * @date 2018/7/31 15:39
 **/

@Controller
@RequestMapping("/manage/product/")
public class ProductManagerController {

    private final IUserService iUserService;
    private final IProductService iProductService;
    private final IFileService iFileService;

    @Autowired
    public ProductManagerController(IUserService iUserService, IProductService iProductService, IFileService iFileService) {
        this.iUserService = iUserService;
        this.iProductService = iProductService;
        this.iFileService = iFileService;
    }

    @RequestMapping(value = "save.do", method = RequestMethod.POST)
    @ResponseBody
    /**保存商品信息
     *@param  [product, session]
     *@return com.mmall.common.ServerResponse
     *@author 卢越
     *@date 2018/8/1
     */
    public ServerResponse insertOrUpdateProduct(Product product, HttpSession session) {
        User user = (User) session.getAttribute(Constants.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeAndMessage(ResponseCode.NEED_LOGIN.getCode(), "请先登录");
        }

        if (iUserService.checkAdminValid(user).isSuccess()) {
            return iProductService.saveOrUpdateProduct(product);
        }

        return ServerResponse.createByErrorMessage("用户权限不足");
    }

    @RequestMapping(value = "set_sale_status.do", method = RequestMethod.GET)
    @ResponseBody
    /**更新商品上下架的状态
     *@param  [productId, status, session]
     *@return com.mmall.common.ServerResponse
     *@author 卢越
     *@date 2018/8/1
     */
    public ServerResponse updateProductStatus(Integer productId, Integer status, HttpSession session) {
        User user = (User) session.getAttribute(Constants.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeAndMessage(ResponseCode.NEED_LOGIN.getCode(), "请先登录");
        }

        if (iUserService.checkAdminValid(user).isSuccess()) {
            return iProductService.setSaleStatus(productId, status);
        }

        return ServerResponse.createByErrorMessage("用户权限不足");
    }

    @RequestMapping(value = "detail.do", method = RequestMethod.GET)
    @ResponseBody
    /**获取商品详情
     *@param  [productId, session]
     *@return com.mmall.common.ServerResponse
     *@author 卢越
     *@date 2018/8/1
     */
    public ServerResponse getProductDetail(Integer productId, HttpSession session) {
        User user = (User) session.getAttribute(Constants.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeAndMessage(ResponseCode.NEED_LOGIN.getCode(), "请先登录");
        }

        if (iUserService.checkAdminValid(user).isSuccess()) {
            return iProductService.getManagerProductDetail(productId);
        }

        return ServerResponse.createByErrorMessage("用户权限不足");
    }

    @RequestMapping(value = "list.do", method = RequestMethod.GET)
    @ResponseBody
    /**获得商品分页
     *@param  [current, size, session]
     *@return com.mmall.common.ServerResponse
     *@author 卢越
     *@date 2018/8/1
     */
    public ServerResponse<PageInfo> getProductList(@RequestParam(defaultValue = "1") int current, @RequestParam(defaultValue = "10") int size, HttpSession session) {
        User user = (User) session.getAttribute(Constants.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeAndMessage(ResponseCode.NEED_LOGIN.getCode(), "请先登录");
        }

        if (iUserService.checkAdminValid(user).isSuccess()) {
            //执行分页查询
            return iProductService.getManageProductList(current, size);
        }

        return ServerResponse.createByErrorMessage("用户权限不足");
    }

    @RequestMapping(value = "search.do", method = RequestMethod.GET)
    @ResponseBody
    /**商品搜索功能
     *@param  [productName, productId, current, size, session]
     *@return com.mmall.common.ServerResponse<com.github.pagehelper.PageInfo>
     *@author 卢越
     *@date 2018/8/1
     */
    public ServerResponse<PageInfo> productSearch(String productName, Integer productId, @RequestParam(defaultValue = "1") int current, @RequestParam(defaultValue = "10") int size, HttpSession session) {
        User user = (User) session.getAttribute(Constants.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeAndMessage(ResponseCode.NEED_LOGIN.getCode(), "请先登录");
        }

        if (iUserService.checkAdminValid(user).isSuccess()) {
            //执行业务
            return iProductService.productSearch(productName, productId, current, size);
        }

        return ServerResponse.createByErrorMessage("用户权限不足");
    }

    @RequestMapping(value = "upload.do", method = RequestMethod.POST)
    @ResponseBody
    /**上传文件
     *@param  [file, request, session]
     *@return com.mmall.common.ServerResponse
     *@author 卢越
     *@date 2018/8/1
     */
    public ServerResponse upload(@RequestParam(value = "upload_file", required = false) MultipartFile file, HttpServletRequest request, HttpSession session) {
        User user = (User) session.getAttribute(Constants.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeAndMessage(ResponseCode.NEED_LOGIN.getCode(), "请先登录");
        }

        if (iUserService.checkAdminValid(user).isSuccess()) {
            String path = request.getSession().getServletContext().getRealPath("upload");
            //执行业务
            String targetFileName = iFileService.upload(file, path);
            String url = PropertiesUtil.getKey("ftp.server.http.prefix") + targetFileName;

            Map<String, String> map = Maps.newHashMap();
            map.put("uri", targetFileName);
            map.put("url", url);

            return ServerResponse.createBySuccess(map);
        }

        return ServerResponse.createByErrorMessage("用户权限不足");
    }


    @RequestMapping(value = "rich_text_img_upload.do", method = RequestMethod.POST)
    @ResponseBody
    /**simditor富文本插件上传接口
     * 返回格式 ：
     * {
     "success": true/false,
     "msg": "error message", # optional
     "file_path": "[real file path]"
     }
     *@param  [file, session, request, response]
     *@return java.util.Map
     *@author 卢越
     *@date 2018/8/1
     */
    public Map richTextImgUpload(@RequestParam(value = "upload_file", required = false) MultipartFile file, HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = Maps.newHashMap();
        User user = (User) session.getAttribute(Constants.CURRENT_USER);
        if (user == null) {
            map.put("success", false);
            map.put("msg", "请先登录管理员");
            return map;
        }

        if (iUserService.checkAdminValid(user).isSuccess()) {
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = iFileService.upload(file, path);
            if (StringUtils.isBlank(targetFileName)) {
                map.put("success", false);
                map.put("msg", "上传失败");
                return map;
            }
            String url = PropertiesUtil.getKey("ftp.server.http.prefix") + targetFileName;
            map.put("success", true);
            map.put("msg", "上传成功");
            map.put("file_path", url);
            response.addHeader("Access-Control-Allow-Headers", "X-File-Name");
            return map;
        }

        map.put("success", false);
        map.put("msg", "无权限操作");
        return map;
    }
}
