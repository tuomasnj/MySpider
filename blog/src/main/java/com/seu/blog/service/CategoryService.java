package com.seu.blog.service;

import com.seu.blog.vo.CategoryVo;
import com.seu.blog.vo.Result;

public interface CategoryService {
    CategoryVo findCategoryById(Long categoryId);

    Result findAll();

    Result findAllDetail();

    Result categoriesDetailById(Long id);
}
