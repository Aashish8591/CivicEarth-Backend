package com.civicearth.backend.service;

import com.civicearth.backend.model.Post;
import com.civicearth.backend.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.civicearth.backend.repository.UserRepository;
import com.civicearth.backend.model.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    // CREATE POST
    public Post create(Post post) {

        // initialize lists if null (important)
        if (post.getLikes() == null) {
            post.setLikes(new ArrayList<>());
        }

        if (post.getComments() == null) {
            post.setComments(new ArrayList<>());
        }

        // fix null fields
        if (post.getCategory() == null) {
            post.setCategory("");
        }

        if (post.getLocation() == null) {
            post.setLocation("");
        }

        if (post.getDepartment() == null) {
            post.setDepartment("");
        }

        post.setCreatedAt(new Date());

        post.setStatus("PENDING");

        return postRepository.save(post);
    }

    public Post getById(String id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
    }

    public Post save(Post post) {
        return postRepository.save(post);
    }

    // GET ALL POSTS
    public List<Post> getAll() {
        return postRepository.findAll();
    }

    // LIKE / UNLIKE POST
    public Post likePost(String postId, String userId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        // fix null issue
        if (post.getLikes() == null) {
            post.setLikes(new ArrayList<>());
        }

        if (post.getLikes().contains(userId)) {
            post.getLikes().remove(userId);
        } else {
            post.getLikes().add(userId);
        }

        return postRepository.save(post);
    }

    // ADD COMMENT
    public Post addComment(String postId, String comment) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (post.getComments() == null) {
            post.setComments(new ArrayList<>());
        }

        post.getComments().add(comment);

        return postRepository.save(post);
    }

    // GET POSTS BY USER
    public List<Map<String, Object>> getPostsByUser(String userId) {

        List<Post> posts = postRepository.findByUserId(userId);

        return posts.stream().map(post -> {

            User user = userRepository.findById(post.getUserId())
                    .orElse(null);

            Map<String, Object> response = new HashMap<>();

            response.put("id", post.getId());
            response.put("content", post.getContent());
            response.put("imageUrl", post.getImageUrl());
            response.put("location", post.getLocation());
            response.put("category", post.getCategory());
            response.put("status", post.getStatus());
            response.put("createdAt", post.getCreatedAt());
            response.put("likes", post.getLikes());
            response.put("comments", post.getComments());

            if (user != null) {

                Map<String, Object> userMap = new HashMap<>();

                userMap.put("id", user.getId());
                userMap.put("fullName", user.getFullName());
                userMap.put("profilePic", user.getProfilePic());

                response.put("user", userMap);

            } else {
                response.put("user", null);
            }

            return response;

        }).toList();
    }

    // GET POSTS BY DEPARTMENT
    public List<Map<String, Object>> getByDepartment(String department) {

        List<Post> posts = postRepository.findByDepartment(department);

        return posts.stream().map(post -> {

            User user = userRepository.findById(post.getUserId())
                    .orElse(null);

            Map<String, Object> response = new HashMap<>();

            response.put("id", post.getId());
            response.put("content", post.getContent());
            response.put("imageUrl", post.getImageUrl());
            response.put("location", post.getLocation());
            response.put("category", post.getCategory());
            response.put("status", post.getStatus());
            response.put("createdAt", post.getCreatedAt());
            response.put("likes", post.getLikes());
            response.put("comments", post.getComments());

            if (user != null) {

                Map<String, Object> userMap = new HashMap<>();

                userMap.put("id", user.getId());
                userMap.put("fullName", user.getFullName());
                userMap.put("profilePic", user.getProfilePic());

                response.put("user", userMap);

            } else {
                response.put("user", null);
            }

            return response;

        }).toList();
    }

    // UPDATE STATUS
    public Post updateStatus(String id, Map<String, String> data) {

        Post post = getById(id);

        post.setStatus(data.get("status"));
        post.setProofImage(data.get("proofImage"));
        post.setAuthorityMessage(data.get("authorityMessage"));

        return postRepository.save(post);
    }

    // GET ALL POSTS WITH USER
    public List<Map<String, Object>> getAllWithUser() {

        List<Post> posts = postRepository.findAll();

        return posts.stream().map(post -> {

            User user = userRepository.findById(post.getUserId())
                    .orElse(null);

            Map<String, Object> response = new HashMap<>();

            response.put("id", post.getId());
            response.put("content", post.getContent());
            response.put("imageUrl", post.getImageUrl());
            response.put("location", post.getLocation());
            response.put("category", post.getCategory());
            response.put("status", post.getStatus());
            response.put("createdAt", post.getCreatedAt());

            if (user != null) {

                Map<String, Object> userMap = new HashMap<>();

                userMap.put("id", user.getId());
                userMap.put("fullName", user.getFullName());
                userMap.put("profilePic", user.getProfilePic());

                response.put("user", userMap);

            } else {
                response.put("user", null);
            }

            return response;

        }).toList();
    }
}