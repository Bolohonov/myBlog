package org.example.blog.api.controller;

import lombok.RequiredArgsConstructor;
import org.example.blog.api.request.CommentRequest;
import org.example.blog.api.request.PostRequest;
import org.example.blog.api.response.PostResponse;
import org.example.blog.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/blog")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

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
}
