package com.seu.blog.controller;

import com.seu.blog.service.LoginService;
import com.seu.blog.vo.Result;
import com.seu.blog.vo.params.LoginParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/login")
public class LoginController {
    @Resource
    private LoginService loginService;

    @PostMapping
    //登录、验证用户
    public Result login(@RequestBody LoginParam loginParam) {
        return loginService.login(loginParam);
    }
}
