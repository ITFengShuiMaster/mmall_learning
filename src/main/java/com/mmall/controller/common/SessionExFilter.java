package com.mmall.controller.common;

import com.mmall.common.Constants;
import com.mmall.pojo.User;
import com.mmall.util.CookieUtil;
import com.mmall.util.ShardedJedisPoolUtil;
import com.mmall.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/** 重置session状态
 * @author Luyue
 * @date 2018/8/13 12:15
 **/
public class SessionExFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    /** 用户登录之后，每一次的访问都重置token的有效期
     *@param  [servletRequest, servletResponse, filterChain]
     *@return  void
     *@author  卢越
     *@date  2018/8/19
     */
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String token = CookieUtil.readCookie(request);
        if (StringUtils.isNotEmpty(token)) {
            User user = JsonUtil.json2Object(ShardedJedisPoolUtil.get(token), User.class);
            if (user != null) {
                ShardedJedisPoolUtil.expire(token, Constants.RedisExTime.EX_TIME);
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
