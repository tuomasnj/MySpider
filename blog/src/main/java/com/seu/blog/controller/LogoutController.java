package com.seu.blog.controller;

import com.seu.blog.service.LoginService;
import com.seu.blog.vo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/logout")
public class LogoutController {

    @Resource
    private LoginService loginService;

    @GetMapping
    //将请求头中参数Authorization的值赋给token
    public Result logout(@RequestHeader("Authorization") String token) {
        return loginService.logout(token);
    }
}
