package org.example.blog.service;

import lombok.RequiredArgsConstructor;
import org.example.blog.api.mapper.PostMapper;
import org.example.blog.api.request.PostRequest;
import org.example.blog.api.response.PostResponse;
import org.example.blog.model.Post;
import org.example.blog.repo.PostRepo;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepo postRepository;
    private final PostMapper postMapper;

    public void save(PostRequest request) {
        postRepository.save(postMapper.toPost(request));
    }

    public PostResponse getById(Long id) {
        return postMapper.toResponse(postRepository.getById(id));
    }

    public void removeById(Long id) {
        postRepository.deleteById(id);
    }

    public void update(Long id, PostRequest request) {
        Post post = postRepository.getById(id);
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setComments();
        post.setTags();
        postRepository.update(post);
    }
}
