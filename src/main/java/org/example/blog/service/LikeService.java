package org.example.blog.service;

import lombok.RequiredArgsConstructor;
import org.example.blog.model.Like;
import org.example.blog.model.Post;
import org.example.blog.repo.LikeRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepo likeRepo;

    public void addLike(Post post) {
        Like like = new Like();
        like.setPostId(post.getId());
        likeRepo.save(like);
    }

    public Map<Long, List<Like>> getByPostIds(List<Long> postIds) {
        List<Like> likes = likeRepo.findByPostIds(postIds);
        return likes.stream().collect(Collectors.groupingBy(Like::getPostId));
    }
}
