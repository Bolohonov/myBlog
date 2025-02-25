package org.example.blog.repo.impl.jdbc;

import org.example.blog.model.Post;
import org.example.blog.repo.PostRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class PostRepoImpl implements PostRepo {
    @Override
    public void save(Post post) {

    }

    @Override
    public Post getById(Long id) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public void update(Post post) {

    }

    @Override
    public Page<Post> findAll(Pageable pageable) {
        return null;
    }
}
