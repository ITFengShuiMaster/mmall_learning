package com.mmall.controller.common.intercept;

import com.google.common.collect.Maps;
import com.mmall.common.Constants;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.Map;

/**
 * @author Luyue
 * @date 2018/8/17 15:23
 **/
@Slf4j
public class AuthorityIntercepter implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        String methodName = handlerMethod.getMethod().getName();

        //代码验证登录接口，并放过，防止登录死循环
//        if ("login".equals(methodName)) {
//            return true;
//        }

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(Constants.CURRENT_USER);
        if (user == null || user.getRole().intValue() != Constants.ROLE.ROLE_ADMIN) {
            response.reset();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=UTF-8");

            PrintWriter pw = response.getWriter();

            if (user == null) {
                if ("richTextImgUpload".equals(methodName)) {
                    Map<String, Object> map = Maps.newHashMap();
                    map.put("success", false);
                    map.put("msg", "请登录");
                    pw.write(JsonUtil.objToJson(map));
                } else {
                    pw.write(JsonUtil.objToJson(ServerResponse.createByErrorCodeAndMessage(ResponseCode.NEED_LOGIN.getCode(), "拦截器拦截，请登录")));
                }
            } else {
                if ("richTextImgUpload".equals(methodName)) {
                    Map<String, Object> map = Maps.newHashMap();
                    map.put("success", false);
                    map.put("msg", "无权限操作");
                    pw.write(JsonUtil.objToJson(map));
                } else {
                    pw.write(JsonUtil.objToJson(ServerResponse.createByErrorCodeAndMessage(ResponseCode.ERROR.getCode(), "拦截器拦截，无权限操作")));
                }
            }

            pw.flush();
            pw.close();
            return false;
        }

        log.info("preHandle");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("postHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("afterCompletion");
    }
}
