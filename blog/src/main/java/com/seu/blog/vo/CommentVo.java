package com.seu.blog.vo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import java.util.List;

@Data
public class CommentVo  {

    //防止前端数据传递时出现精度损失，把id转为String
    @JsonSerialize(using= ToStringSerializer.class)
    private String id;

    private UserVo author;

    private String content;

    private List<CommentVo> children;

    private String createDate;

    private Integer level;

    private UserVo toUser;
}
