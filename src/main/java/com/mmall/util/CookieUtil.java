package com.mmall.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Luyue
 * @date 2018/8/12 14:40
 **/
@Slf4j
public class CookieUtil {

    private static final String COOKIE_DOMAIN = ".luyue.com";
    private static final String COOKIE_NAME = "mmall_login_token";

    public static void writeCookie(String token, HttpServletResponse response) {
        Cookie cookie = new Cookie(COOKIE_NAME, token);
        cookie.setDomain(COOKIE_DOMAIN);
        cookie.setPath("/");
        //如果设置-1 , 代表cookie 永久有效
        cookie.setMaxAge(60 * 60 * 24 * 365);
        cookie.setHttpOnly(true);

        log.info("write cookie name:{} value:{}", cookie.getName(), cookie.getValue());

        response.addCookie(cookie);
    }

    public static String readCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }

        for (Cookie c : cookies) {
            if (StringUtils.equals(c.getName(), COOKIE_NAME)) {
                log.info("read cookie name:{} value:{}", c.getName(), c.getValue());
                return c.getValue();
            }
        }

        return null;
    }

    public static void delCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie c: cookies) {
                if (StringUtils.equals(c.getName(), COOKIE_NAME)) {
                    log.info("del cookie name:{} value:{}", c.getName(), c.getValue());
                    c.setDomain(COOKIE_DOMAIN);
                    c.setPath("/");
                    c.setMaxAge(0);

                    response.addCookie(c);
                    break;
                }
            }
        }
    }
}
