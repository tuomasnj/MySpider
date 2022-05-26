package com.seu.blog.controller;

import com.seu.blog.utils.QiniuUtils;
import com.seu.blog.vo.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.UUID;

@RestController
@RequestMapping("/upload")
public class UploadController {
    @Resource
    QiniuUtils qiniuUtils;

    @PostMapping
    public Result upload(@RequestParam("image") MultipartFile file){
        //原始文件名称 比如hello.png
        String originalFileName = file.getOriginalFilename();

        //唯一的文件名称
        String fileName = UUID.randomUUID().toString()+"."+ StringUtils.substringAfterLast(originalFileName,".");

        //从七牛云服务器加载用户上传的图片
        boolean upload = qiniuUtils.upload(file, fileName);
        if (upload){
            return Result.success(QiniuUtils.url + fileName);
        }
        return Result.fail(20001,"上传失败");
    }
}
