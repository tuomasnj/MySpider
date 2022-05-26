package com.seu.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.seu.blog.dao.pojo.SysUser;
import com.seu.blog.service.LoginService;
import com.seu.blog.service.SysUserService;
import com.seu.blog.utils.JWTUtils;
import com.seu.blog.vo.ErrorCode;
import com.seu.blog.vo.Result;
import com.seu.blog.vo.params.LoginParam;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl implements LoginService {
    private static final String salt = "mszlu!@#";
    @Resource
    private SysUserService sysUserService;

    @Resource
    private RedisTemplate<String,String> redisTemplate;
    @Override
    public Result login(LoginParam loginParam) {
        /**
         * 1.检查参数是否合法
         * 2.根据用户名和密码去user表中查询是否存在用户
         * 3.如果不存在 登录失败
         * 4.如果存在，使用jwt生成token返回给前端
         * 5.token放入redis当中，token: user
         * 信息设置过期时间(登录的时候先认证token字符串是否合法)
         */

        String account =loginParam.getAccount();
        String password=loginParam.getPassword();

        //1.检查参数是否合法
        if(StringUtils.isBlank(account) || StringUtils.isBlank(password)){
            //枚举类统一管理错误信息
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
        password= DigestUtils.md5Hex(password+salt);
        //2.根据用户名和密码去user表中查询是否存在用户
        SysUser sysUser= sysUserService.findUser(account,password);

        //3.如果不存在 登录失败
        if(sysUser==null){
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(),ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }

        //4.如果存在，使用jwt生成token返回给前端
        String token= JWTUtils.createToken(sysUser.getId());

        //5.把token及用户信息存入Redis
        ValueOperations<String, String> stringStringValueOperations = redisTemplate.opsForValue();
        stringStringValueOperations.set("TOKEN_"+token, JSON.toJSONString(sysUser),1, TimeUnit.DAYS);

        return Result.success(token);
    }

    @Override
    public SysUser checkToken(String token) {
        /**
         * 1. token合法性校验
         * 是否为空，解析是否成功 redis是否存在
         *
         * 2.如果校验失败返回错误
         * 3.如果成功，返回对应的结果
         */
        if(StringUtils.isBlank(token)){
            return null;
        }

        Map<String,Object> stringObjectMap = JWTUtils.checkToken(token);
        if(stringObjectMap==null){
            return null;
        }

        String userJson=redisTemplate.opsForValue().get("TOKEN_" + token);

        if(StringUtils.isBlank(userJson)){
            return null;
        }

        SysUser sysUser = JSON.parseObject(userJson, SysUser.class);

        return sysUser;
    }

    @Override
    public Result logout(String token) {
        redisTemplate.delete("TOKEN_"+token);
        return Result.success(null);
    }


    @Override
    public Result register(LoginParam loginParam) {
        /**
         * 1.判断参数是否合法
         * 2.判断用户是否存在，如果存在 返回账户已经被注册
         * 3.不存在，注册用户
         * 4.生成token
         * 5.存入redis并返回，注册完成后直接登录
         * 6.需要加上事务，一旦中间的任何过程出问题就回滚
         */

        String account = loginParam.getAccount();
        String password = loginParam.getPassword();
        String nickName = loginParam.getNickname();

        if(StringUtils.isBlank(account) || StringUtils.isBlank(password) || StringUtils.isBlank(nickName)){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }

        SysUser sysUser= sysUserService.findUserByAccount(account);

        if(sysUser !=null){
            return Result.fail(ErrorCode.ACCOUNT_EXIST.getCode(),ErrorCode.ACCOUNT_EXIST.getMsg());
        }

        sysUser = new SysUser();
        sysUser.setNickname(nickName);
        sysUser.setAccount(account);
        sysUser.setPassword(DigestUtils.md5Hex(password+salt));
        sysUser.setCreateDate(System.currentTimeMillis());
        sysUser.setLastLogin(System.currentTimeMillis());
        sysUser.setAvatar("/static/img/logo.b3a48c0.png");
        sysUser.setAdmin(1); //1 代表true
        sysUser.setDeleted(0); // 0 代表false
        sysUser.setSalt("");
        sysUser.setStatus("");
        sysUser.setEmail("");
        sysUserService.save(sysUser);

        String token = JWTUtils.createToken(sysUser.getId());
        redisTemplate.opsForValue().set("TOKEN_"+token, JSON.toJSONString(sysUser),1, TimeUnit.DAYS);
        System.out.println(token);
        return Result.success(token);
    }
}
