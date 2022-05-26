package com.seu.blog.handler;

import com.alibaba.fastjson.JSON;
import com.seu.blog.dao.pojo.SysUser;
import com.seu.blog.service.LoginService;
import com.seu.blog.utils.UserThreadLocal;
import com.seu.blog.vo.ErrorCode;
import com.seu.blog.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    @Resource
    LoginService loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        //该方法在执行Controller方法之前执行
        /**
         * 1.判断请求的接口路径是否为HandlerMethod(controller方法)
         * 2.判断token是否为空，如果为空就是未登录
         * 3.如果token不为空，进行登录验证 loginService checkToken
         * 4.如果验证成功 放行
         */

        if(!(handler instanceof HandlerMethod)){
            //handler 可能是RequestResourceHandler 访问静态资源放行
            return true;
        }

        String token=request.getHeader("Authorization");
        log.info("=================request start===========================");
        String requestURI = request.getRequestURI();
        log.info("request uri:{}",requestURI);
        log.info("request method:{}",request.getMethod());
        log.info("token:{}", token);
        log.info("=================request end===========================");

        if(StringUtils.isBlank(token)){
            Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(), ErrorCode.NO_LOGIN.getMsg());
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        }
        SysUser sysUser= loginService.checkToken(token);
        if(sysUser==null){
            Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(), ErrorCode.NO_LOGIN.getMsg());
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        }

        UserThreadLocal.put(sysUser);
        return true;
    }

    @Override
    /**
     * 在线程关闭后删除ThreadLocal中的用户信息，避免内存泄漏
     */
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        UserThreadLocal.remove();
    }
}
