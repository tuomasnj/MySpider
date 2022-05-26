package com.seu.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seu.blog.dao.dos.Archives;
import com.seu.blog.dao.mapper.ArticleBodyMapper;
import com.seu.blog.dao.mapper.ArticleMapper;
import com.seu.blog.dao.mapper.ArticleTagMapper;
import com.seu.blog.dao.pojo.Article;
import com.seu.blog.dao.pojo.ArticleBody;
import com.seu.blog.dao.pojo.SysUser;
import com.seu.blog.service.*;
import com.seu.blog.utils.UserThreadLocal;
import com.seu.blog.vo.ArticleBodyVo;
import com.seu.blog.vo.ArticleVo;
import com.seu.blog.vo.Result;
import com.seu.blog.vo.TagVo;
import com.seu.blog.vo.params.ArticleParam;
import com.seu.blog.vo.params.ArticleTag;
import com.seu.blog.vo.params.PageParams;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Resource
    private ArticleBodyMapper articleBodyMapper;

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private ArticleTagMapper articleTagMapper;

    @Resource
    private SysUserService sysUserService;

    @Resource
    private TagService tagService;

    @Override
    public Result listArticle(PageParams pageParams) {
        Page<Article> page = new Page<>(pageParams.getPage(),pageParams.getPageSize());
        IPage<Article> articleIPage = articleMapper.listArticle(page,pageParams.getCategoryId(),pageParams.getTagId(),pageParams.getYear(),pageParams.getMonth());
        return Result.success(copyList(articleIPage.getRecords(),true,true));
    }
    /**
     * 最热文章
     * @param limit
     * @return
     */
    @Override
    public Result hotArticle(Integer limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getViewCounts);
        queryWrapper.select(Article::getId,Article::getTitle);
        queryWrapper.last("limit "+limit);
        //上述sql语句=select id,title from article order by view_counts desc limit 5
        List<Article> articleList= articleMapper.selectList(queryWrapper);
        return Result.success(copyList(articleList,false,false));
    }

    /**
     * 最新文章
     * @param limit
     * @return
     */
    @Override
    public Result newArticle(Integer limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getCreateDate);
        queryWrapper.select(Article::getId,Article::getTitle);
        queryWrapper.last("limit "+limit);
        //上述sql语句=select id,title from article order by create_date desc limit 5
        List<Article> articleList= articleMapper.selectList(queryWrapper);
        return Result.success(copyList(articleList,false,false));
    }

    @Override
    public Result listArchives() {
        List<Archives> archivesList= articleMapper.listArchives();
        return Result.success(archivesList);
    }

    @Resource
    private ThreadService threadService;

    @Override
    public Result findArticleById(Long articleId) {
        /**
         * 1.根据id查询文章信息
         * 2.根据bodyId和categoryId做关联查询
         */
        Article article = articleMapper.selectById(articleId);
        ArticleVo articleVo = copy(article, true, true,true,true);

        //把更新阅读次数的操作放在线程池中进行，与主线程分离
        threadService.updateArticleViewCount(articleMapper,article);

        return Result.success(articleVo);
    }

    @Override
    public Result publish(ArticleParam articleParam) {
        /**
         * 1、发布文章 也就是构建Article对象
         * 2、作者id 当前的登录用户
         * 3、标签 要将标签加入到关联列表当中
         */
        SysUser sysUser= UserThreadLocal.get();
        Article article = new Article();
        article.setAuthorId(sysUser.getId());
        article.setCategoryId(Long.parseLong(articleParam.getCategory().getId()));
        article.setCreateDate(System.currentTimeMillis());
        article.setCommentCounts(0);
        article.setSummary(articleParam.getSummary());
        article.setTitle(articleParam.getTitle());
        article.setViewCounts(0);
        article.setWeight(Article.Article_Common);

        //插入之后会生成一个文章id
        articleMapper.insert(article);

        //tag
        List<TagVo> tagVos= articleParam.getTags();
        if(tagVos !=null){
            for (TagVo tag : tagVos) {
                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticleId(article.getId());
                articleTag.setTagId(Long.parseLong(tag.getId()));
                articleTagMapper.insert(articleTag);
            }
        }

        //body
        ArticleBody articleBody=new ArticleBody();
        articleBody.setArticleId(article.getId());
        articleBody.setContent(articleParam.getBody().getContent());
        articleBody.setContentHtml(articleParam.getBody().getContentHtml());
        articleBodyMapper.insert(articleBody);

        article.setBodyId(articleBody.getId());
        articleMapper.updateById(article);
        ArticleVo articleVo = new ArticleVo();
        articleVo.setId(String.valueOf(article.getId()));
        return Result.success(articleVo);
    }

    private List<ArticleVo> copyList(List<Article> articles,boolean isTag,boolean isAuthor) {
        List<ArticleVo> list = new ArrayList<>();
        for (Article article : articles) {
            //这里不需要返回Body和Category
            list.add(copy(article,isTag,isAuthor,false,false));
        }
        return list;
    }

    @Resource
    private CategoryService categoryService;

    private ArticleVo copy(Article article, boolean isTag, boolean isAuthor,boolean isBody,boolean isCategory) {
        ArticleVo articleVo = new ArticleVo();
        BeanUtils.copyProperties(article, articleVo);
        articleVo.setId(String.valueOf(article.getId()));
        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd:HH:mm"));

        //给文章添加标签
        if(isTag){
            List<TagVo> tagsByArticleId = tagService.findTagsByArticleId(article.getId());
            articleVo.setTags(tagsByArticleId);
        }
        if(isAuthor){
            Long authorId = article.getAuthorId();
            articleVo.setAuthor(sysUserService.findUserById(authorId).getNickname());
        }
        if(isBody){
            Long bodyId = article.getBodyId();
            articleVo.setBody(findArticleBodyById(bodyId));
        }
        if(isCategory){
            Long categoryId = article.getCategoryId();
            articleVo.setCategory(categoryService.findCategoryById(categoryId));
        }
        return articleVo;
    }

    private ArticleBodyVo findArticleBodyById(Long bodyId) {
        ArticleBody articleBody = articleBodyMapper.selectById(bodyId);
        ArticleBodyVo articleBodyVo=new ArticleBodyVo();
        articleBodyVo.setContent(articleBody.getContent());
        return articleBodyVo;
    }
}
