package com.seu.blog.handler;
import com.seu.blog.vo.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

//异常处理
//对加了@Controller注解的方法进行拦截处理AOP的实现
@ControllerAdvice
public class AllExceptionHandler {
    //处理Exception.class类型的异常
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result doException(Exception e){
        e.printStackTrace();
        return Result.fail(404,"系统异常");
    }
}
