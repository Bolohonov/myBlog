package org.example.blog.repo;

import org.example.blog.model.Like;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeRepo {
    void save(Like like);
    List<Like> findByPostIds(List<Long> postIds);
}
