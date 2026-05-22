package com.civicearth.backend.controller;

import com.civicearth.backend.model.Post;
import com.civicearth.backend.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin("*")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping
    public Post createPost(@RequestBody Post post) {
        return postService.create(post);
    }

    @GetMapping
    public List<?> getPosts() {
        return postService.getAllWithUser();
    }

    @PostMapping("/{id}/like")
    public Post likePost(@PathVariable String id, @RequestParam String userId) {
        return postService.likePost(id, userId);
    }

    @PostMapping("/{id}/comment")
    public Post addComment(@PathVariable String id, @RequestParam String text) {
        return postService.addComment(id, text);
    }

    @GetMapping("/user/{userId}")
    public List<Map<String, Object>> getUserPosts(@PathVariable String userId) {
        return postService.getPostsByUser(userId);
    }

    // 🔥 NEW STATUS UPDATE API
    @PutMapping("/{id}/status")
    public Post updateStatus(
            @PathVariable String id,
            @RequestBody Map<String, String> data
    ) {
        Post post = postService.getById(id);

        post.setStatus(data.get("status"));
        post.setProofImage(data.get("proofImage"));
        post.setAuthorityMessage(data.get("authorityMessage"));

        return postService.save(post);
    }

    @GetMapping("/department/{dept}")
    public List<Map<String, Object>> getByDepartment(@PathVariable String dept) {
        return postService.getByDepartment(dept);
    }

    @GetMapping("/{id}")
    public Post getPostById(@PathVariable String id) {
        return postService.getById(id);
    }

}