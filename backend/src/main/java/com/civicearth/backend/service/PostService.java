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

        // fix null issue (VERY IMPORTANT)
        if (post.getLikes() == null) {
            post.setLikes(new ArrayList<>());
        }

        if (post.getLikes().contains(userId)) {
            post.getLikes().remove(userId); // unlike
        } else {
            post.getLikes().add(userId); // like
        }

        return postRepository.save(post);
    }

    // ADD COMMENT
    public Post addComment(String postId, String comment) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        // fix null issue (VERY IMPORTANT)
        if (post.getComments() == null) {
            post.setComments(new ArrayList<>());
        }

        post.getComments().add(comment);


        return postRepository.save(post);
    }


//    public List<Post> getPostsByUser(String userId) {
//        return postRepository.findByUserId(userId);
//    }


    public List<Map<String, Object>> getPostsByUser(String userId) {

        List<Post> posts = postRepository.findByUserId(userId);

        return posts.stream().map(post -> {

            User user = userRepository.findById(post.getUserId())
                    .orElse(null);

            return Map.of(
                    "id", post.getId(),
                    "content", post.getContent(),
                    "imageUrl", post.getImageUrl(),
                    "location", post.getLocation(),
                    "category", post.getCategory(),
                    "status", post.getStatus(),
                    "createdAt", post.getCreatedAt(),
                    "likes", post.getLikes(),
                    "comments", post.getComments(),

                    "user", user != null ? Map.of(
                            "id", user.getId(),
                            "fullName", user.getFullName(),
                            "profilePic", user.getProfilePic()
                    ) : null
            );
        }).toList();
    }

    public List<Map<String, Object>> getByDepartment(String department) {

        List<Post> posts = postRepository.findByDepartment(department);

        return posts.stream().map(post -> {

            User user = userRepository.findById(post.getUserId())
                    .orElse(null);

            return Map.of(
                    "id", post.getId(),
                    "content", post.getContent(),
                    "imageUrl", post.getImageUrl(),
                    "location", post.getLocation(),
                    "category", post.getCategory(),
                    "status", post.getStatus(),
                    "createdAt", post.getCreatedAt(),
                    "likes", post.getLikes(),
                    "comments", post.getComments(),

                    "user", user != null ? Map.of(
                            "id", user.getId(),
                            "fullName", user.getFullName(),
                            "profilePic", user.getProfilePic()
                    ) : null
            );
        }).toList();
    }

    public Post updateStatus(String id, Map<String, String> data) {
        Post post = getById(id);

        post.setStatus(data.get("status"));
        post.setProofImage(data.get("proofImage"));
        post.setAuthorityMessage(data.get("authorityMessage"));

        return postRepository.save(post);
    }

    public List<Map<String, Object>> getAllWithUser() {
        List<Post> posts = postRepository.findAll();

        return posts.stream().map(post -> {
            User user = userRepository.findById(post.getUserId()).orElse(null);

            return Map.of(
                    "id", post.getId(),
                    "content", post.getContent(),
                    "imageUrl", post.getImageUrl(),
                    "location", post.getLocation(),
                    "category", post.getCategory(),
                    "status", post.getStatus(),
                    "createdAt", post.getCreatedAt(),

                    // 🔥 SAFE USER DATA ONLY
                    "user", user != null ? Map.of(
                            "id", user.getId(),
                            "fullName", user.getFullName(),
                            "profilePic", user.getProfilePic()
                    ) : null
            );
        }).toList();
    }
}