package org.example.blog.mapper;

import org.example.blog.api.request.CommentRequest;
import org.example.blog.model.Comment;
import org.example.blog.model.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface CommentMapper {
    @Mapping(target = "postId", source = "post.id")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    Comment toComment(CommentRequest request, Post post);
}
