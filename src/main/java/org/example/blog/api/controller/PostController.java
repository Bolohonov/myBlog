package org.example.blog.api.controller;

import lombok.RequiredArgsConstructor;
import org.example.blog.api.request.CommentRequest;
import org.example.blog.api.request.PostRequest;
import org.example.blog.api.response.PostResponse;
import org.example.blog.service.PostService;
import org.example.blog.service.TagService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/blog")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final TagService tagService;

    @PostMapping(path = "/post")
    public String savePost(@ModelAttribute PostRequest postDto) {
        postService.save(postDto);
        return "redirect:/api/blog";
    }

    @GetMapping(path = "/post/{id}")
    public String getPost(@PathVariable("id") Long id, Model model) {
        PostResponse post = postService.getById(id);
        model.addAttribute("post", post);
        return "post";
    }

    @DeleteMapping(path = "/post/{id}")
    public String deletePost(@PathVariable("id") Long id) {
        postService.removeById(id);
        return "redirect:/api/blog";
    }

    @PutMapping(path = "/post/{id}/update")
    public String update(@ModelAttribute PostRequest request, @PathVariable("id") Long id) {
        postService.update(id, request);
        return "redirect:/api/blog";
    }

    @PostMapping(path = "/post/{id}/comment")
    public String addComment(@PathVariable("id") Long id, @ModelAttribute CommentRequest comment) {
        postService.addComment(id, comment);
        return "redirect:/api/blog/post/" + id;
    }

    @DeleteMapping(path = "/post/{id}/comment/{commentId}")
    public void deleteComment(@PathVariable("id") Long id, @PathVariable("commentId") Long commentId) {
        postService.deleteComment(id, commentId);
    }

    @GetMapping(path = "/post/{id}/like")
    public void likePost(@PathVariable("id") Long id) {
        postService.like(id);
    }

    @GetMapping
    public String getPosts(@PageableDefault Pageable pageable, Model model) {
        List<String> tags = tagService.getAllTags();
        Page<PostResponse> posts = postService.getPosts(pageable);
        model.addAttribute("posts", posts.getContent());
        model.addAttribute("currentPage", posts.getNumber() + 1);
        model.addAttribute("totalPages", posts.getTotalPages());
        model.addAttribute("tags", tags);
        return "posts-feed";
    }
}
