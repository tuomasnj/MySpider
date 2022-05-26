package com.seu.blog.controller;

import com.seu.blog.dao.pojo.SysUser;
import com.seu.blog.utils.UserThreadLocal;
import com.seu.blog.vo.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class TestController {
    @RequestMapping
    public Result test(){
        SysUser sysUser= UserThreadLocal.get();
        System.out.println(sysUser);
        return Result.success(sysUser);
    }
}