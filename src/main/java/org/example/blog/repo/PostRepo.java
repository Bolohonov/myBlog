package org.example.blog.repo;

import org.example.blog.model.Post;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepo {
    void save(Post post);
    Post getById(Long id);
    void deleteById(Long id);
    void update(Post post);
}
