package com.seu.blog.dao.pojo;

import lombok.Data;

@Data
//mybatisPlus采用驼峰映射规则
//例如MyUserTable对应的数据库表为my_user_table;TEMyUserTable 对应表名为t_e_my_user_table
public class Article {

    public static final int Article_TOP = 1;

    public static final int Article_Common = 0;

    private Long id;

    private String title;

    private String summary;

    private Integer commentCounts;

    private Integer viewCounts;

    /**
     * 作者id
     */
    private Long authorId;
    /**
     * 内容id
     */
    private Long bodyId;
    /**
     * 类别id
     */
    private Long categoryId;

    /**
     * 置顶
     */
    private Integer weight;


    /**
     * 创建时间
     */
    private Long createDate;
}
