package com.mmall.controller.common;

import com.mmall.common.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** springMVC 全局异常处理
 * @author Luyue
 * @date 2018/8/17 14:53
 **/
@Slf4j
@Component
public class ExceptionResolvor implements HandlerExceptionResolver {
    @Override
    /** 全局异常处理，让后台程序运行过程中的错误信息不用暴露到前台
     *@param  [httpServletRequest, httpServletResponse, o, e]
     *@return  org.springframework.web.servlet.ModelAndView
     *@author  卢越
     *@date  2018/8/19
     */
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        log.error("{} is error", httpServletRequest.getRequestURI(), e);
        ModelAndView modelAndView = new ModelAndView(new MappingJacksonJsonView());
        modelAndView.addObject("status", ResponseCode.ERROR.getCode());
        modelAndView.addObject("msg", "系统运行过程中出现异常，请查看后台日志");
        modelAndView.addObject("data", e.toString());
        return modelAndView;
    }
}
