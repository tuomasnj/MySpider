package com.seu.blog.controller;

import com.seu.blog.common.aop.LogAnnotation;
import com.seu.blog.common.cache.Cache;
import com.seu.blog.service.ArticleService;
import com.seu.blog.vo.Result;
import com.seu.blog.vo.params.ArticleParam;
import com.seu.blog.vo.params.PageParams;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/articles")
public class ArticleController {
    @Resource
    private ArticleService articleService;


    @PostMapping
    @LogAnnotation(module="文章",operator="获取文章列表")
    @Cache(expire = 5*60,name = "111")
    public Result listArticle(@RequestBody PageParams pageParams){
        return articleService.listArticle(pageParams);
    }

    @Cache(expire = 5*1000*60,name = "222")
    @PostMapping("/hot")
    public Result hotArticle(){
        Integer limit=5;
        return articleService.hotArticle(limit);
    }

    @Cache(expire = 5*1000*60,name = "333")
    @PostMapping("/new")
    public Result newArticle(){
        Integer limit=5;
        return articleService.newArticle(limit);
    }
    @PostMapping("/listArchives")
    public Result listArchives(){
        return  articleService.listArchives();
    }

    @PostMapping("/view/{id}")
    public Result findArticleById(@PathVariable("id") Long articleId){
        return articleService.findArticleById(articleId);
    }

    //接收前端传递的ArticleParam参数
    @PostMapping("/publish")
    public Result publish(@RequestBody ArticleParam articleParam){
        return articleService.publish(articleParam);
    }
}
