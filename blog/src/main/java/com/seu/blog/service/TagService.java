package com.seu.blog.service;

import com.seu.blog.vo.Result;
import com.seu.blog.vo.TagVo;

import java.util.List;

public interface TagService {
    /**
     *根据文章id查询标签列表
     * @param articleId
     * @return
     */
    List<TagVo> findTagsByArticleId(Long articleId);

    Result hots(int limit);

    /**
     * 查询所有的文章标签
     * @return
     */
    Result findAll();

    Result findAllDetail();

    Result findDetailById(Long id);
}
