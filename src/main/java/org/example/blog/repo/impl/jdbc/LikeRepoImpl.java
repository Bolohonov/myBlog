package org.example.blog.repo.impl.jdbc;

import org.example.blog.model.Like;
import org.example.blog.repo.LikeRepo;

import java.util.List;

public class LikeRepoImpl implements LikeRepo {
    @Override
    public void save(Like like) {

    }

    @Override
    public List<Like> getByPostIds(List<Long> postIds) {
        return List.of();
    }
}
