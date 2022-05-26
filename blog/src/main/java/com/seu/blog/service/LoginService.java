package com.seu.blog.service;
import com.seu.blog.dao.pojo.SysUser;
import com.seu.blog.vo.Result;
import com.seu.blog.vo.params.LoginParam;
import org.springframework.transaction.annotation.Transactional;

@Transactional //加事务注解，防止注册出现异常 可以回滚
public interface LoginService {
    /**
     * 登录功能实现
     * @param loginParam
     * @return
     */
    Result login(LoginParam loginParam);


    /**
     * 检验token是否合法
     * @param token
     * @return
     */
    SysUser checkToken(String token);

    Result logout(String token);

    /**
     * 注册业务
     * @param loginParam
     * @return
     */
    Result register(LoginParam loginParam);
}
