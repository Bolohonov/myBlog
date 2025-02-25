package org.example.blog.service;

import lombok.RequiredArgsConstructor;
import org.example.blog.model.Like;
import org.example.blog.model.Post;
import org.example.blog.repo.LikeRepo;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepo likeRepo;

    public void addLike(Post post) {
        Like like = new Like();
        like.setPostId(post.getId());
        likeRepo.save(like);
    }
}
