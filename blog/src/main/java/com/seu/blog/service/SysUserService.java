package com.seu.blog.service;

import com.seu.blog.dao.pojo.SysUser;
import com.seu.blog.vo.Result;
import com.seu.blog.vo.UserVo;

public interface SysUserService {
    SysUser findUserById(Long id);

    SysUser findUser(String account, String password);

    Result findUserByToken(String token);

    void save(SysUser sysUser);

    SysUser findUserByAccount(String account);

    UserVo findUserVoById(Long authorId);
}
