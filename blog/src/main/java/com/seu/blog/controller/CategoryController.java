package com.seu.blog.controller;

import com.seu.blog.service.CategoryService;
import com.seu.blog.vo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RequestMapping("/categorys")
@RestController
public class CategoryController {
    @Resource
    CategoryService categoryService;

    @GetMapping
    public Result categories(){
        return categoryService.findAll();
    }

    @GetMapping("/detail")
    public Result categoriesDetail(){
        return categoryService.findAllDetail();
    }

    @GetMapping("/detail/{id}")
    public Result categoriesDetailById(@PathVariable("id") Long id){
        return categoryService.categoriesDetailById(id);
    }
}
