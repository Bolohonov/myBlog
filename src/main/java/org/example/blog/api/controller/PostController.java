package org.example.blog.api.controller;

import lombok.RequiredArgsConstructor;
import org.example.blog.api.request.PostRequest;
import org.example.blog.api.response.PostResponse;
import org.example.blog.service.PostService;
import org.springframework.http.MediaType;
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
        return "redirect:/blog";
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
        return "redirect:/blog";
    }

    @PutMapping(path = "/post/{id}/update")
    public String update(@ModelAttribute PostRequest request, @PathVariable("id") Long id) {
        postService.update(id, request);
        return "redirect:/blog";
    }
}
