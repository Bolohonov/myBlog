package org.example.blog.repo.impl.jdbc;

import org.example.blog.model.Comment;
import org.example.blog.repo.CommentRepo;

import java.util.List;

public class CommentRepoImpl implements CommentRepo {
    @Override
    public void save(Comment comment) {

    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public List<Comment> findByPostIds(List<Long> postIds) {
        return List.of();
    }
}
