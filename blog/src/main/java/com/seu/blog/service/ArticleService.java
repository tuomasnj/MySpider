package com.seu.blog.service;

import com.seu.blog.vo.params.ArticleParam;
import com.seu.blog.vo.params.PageParams;
import com.seu.blog.vo.Result;


public interface ArticleService {
    /**
     * 分页查询 文章列表
     */
    Result listArticle(PageParams pageParams);

    /**
     * 最热文章
     * @param limit
     * @return
     */
    Result hotArticle(Integer limit);

    /**
     * 最新文章
     * @param limit
     * @return
     */
    Result newArticle(Integer limit);


    /**
     * 文章归档
     * @return
     */
    Result listArchives();


    /**
     * 查看文章详情
     * @param articleId
     * @return
     */
    Result findArticleById(Long articleId);

    Result publish(ArticleParam articleParam);
}
