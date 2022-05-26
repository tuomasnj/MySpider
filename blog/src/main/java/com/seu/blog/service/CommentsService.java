package com.seu.blog.service;

import com.seu.blog.vo.Result;
import com.seu.blog.vo.params.CommentParam;

public interface CommentsService {
    Result commentsByArticleId(Long id);

    Result comment(CommentParam commentParam);
}
