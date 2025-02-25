package org.example.blog.service;

import lombok.RequiredArgsConstructor;
import org.example.blog.api.mapper.PostMapper;
import org.example.blog.api.request.CommentRequest;
import org.example.blog.api.request.PostRequest;
import org.example.blog.api.response.PostResponse;
import org.example.blog.model.Comment;
import org.example.blog.model.Like;
import org.example.blog.model.Post;
import org.example.blog.model.Tag;
import org.example.blog.repo.PostRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepo postRepository;
    private final PostMapper postMapper;
    private final CommentService commentService;
    private final LikeService likeService;
    private final TagService tagService;

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
        post.setTags(tagService.save(request.getTags()));
        postRepository.update(post);
    }

    public void addComment(Long id, CommentRequest comment) {
        commentService.save(comment, postRepository.getById(id));
    }

    public void deleteComment(Long id, Long commentId) {
        Post post = postRepository.getById(id);
        if (!Objects.equals(post, null) && !CollectionUtils.isEmpty(post.getComments())) {
            post.getComments().stream()
                    .filter(comment -> comment.getId().equals(commentId))
                    .findFirst()
                    .ifPresent(commentService::remove);
        }
    }

    public void like(Long id) {
        likeService.addLike(postRepository.getById(id));
    }

    public Page<PostResponse> getPosts(Pageable pageable) {
        Page<Post> posts = postRepository.findAll(pageable);
        List<Long> postIds = posts.stream().map(Post::getId).toList();
        Map<Long, List<Tag>> tags = tagService.getByPostIds(postIds);
        Map<Long, List<Comment>> comments = commentService.getByPostIds(postIds);
        Map<Long, List<Like>> likes = likeService.getByPostIds(postIds);
        posts.forEach(post -> {
            post.getTags().addAll(tags.getOrDefault(post.getId(), Collections.emptyList()));
            post.getComments().addAll(comments.getOrDefault(post.getId(), Collections.emptyList()));
            post.getLikes().addAll(likes.getOrDefault(post.getId(), Collections.emptyList()));
        });
        return posts.map(postMapper::toResponse);
    }


}
