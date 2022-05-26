package com.seu.blog.dao.dos;
import lombok.Data;

/**
 * dos包用于存放一些非持久化的数据库查询类型，是对pojo实体类的补充
 */
@Data
public class Archives {

    private Integer year;

    private Integer month;

    private Integer count;

}
