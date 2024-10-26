package com.sugaram.controller;

import com.sugaram.entity.Comment;
import com.sugaram.entity.Post;
import com.sugaram.payload.AddPostDTO;
import com.sugaram.repository.CommentRepository;
import com.sugaram.repository.PostRepository;
import com.sugaram.repository.UserRepository;
import com.sugaram.utils.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.lang.module.ResolutionException;
import java.util.ArrayList;
import java.util.function.Supplier;

@RequiredArgsConstructor
@RestController
@RequestMapping("/post")
public class PostController {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    @GetMapping
    public ResponseEntity<?> getPosts(Integer page, Integer size) {
        Page<Post> posts = postRepository.findAllByDisabled(false, PageRequest.of(page, size));
        return ResponseEntity.ok(posts.stream().toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPostsBy(@PathVariable Long id) {
        return ResponseEntity.ok(postRepository.findByIdAndDisabled(id, false).orElseThrow(() ->
             new RuntimeException("Post not found!")
        ));
    }


    @GetMapping("/my")
    public ResponseEntity<?> getPostsByUser(Integer page, Integer size) {
        return ResponseEntity.ok(postRepository.findAllByAuthorAndDisabled(Util.getCurrentUser(), false, PageRequest.of(page, size)).stream().toList());
    }

    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody AddPostDTO postDTO) {
        Post post = Post.builder()
                .body(postDTO.body())
                .title(postDTO.title())
                .likes(0)
                .views(0)
                .disabled(false)
                .comments(new ArrayList<Comment>())
                .build();
        return ResponseEntity.ok(postRepository.save(post));
    }

    @PutMapping
    public ResponseEntity<?> updatePost(@RequestBody AddPostDTO postDTO, @RequestParam Long id) {
        Post post = postRepository.findByIdAndDisabled(id, false).orElseThrow(() ->
                new RuntimeException("Id is incorrect!")
        );
        post.setTitle(postDTO.title() == null ? post.getTitle() : postDTO.title());
        post.setBody(postDTO.body() == null ? post.getBody() : postDTO.body());
        return ResponseEntity.ok(postRepository.save(post));
    }

    @DeleteMapping
    public ResponseEntity<?> deletePost(@RequestParam Long id) {
        Post post = postRepository.findByIdAndAuthorAndDisabled(id, Util.getCurrentUser(), false).orElseThrow(() ->
        new RuntimeException("Id is incorrect!"));
        post.setDisabled(true);
        postRepository.save(post);
        return ResponseEntity.ok().body("Post deleted successfully!");
    }

    @GetMapping("/comment")
    public ResponseEntity<?> getComments(@RequestParam Long postId) {
        return ResponseEntity.ok(postRepository.findByIdAndDisabled(postId, false).orElseThrow(() ->
                new RuntimeException("Id is incorrect!")).getComments().stream()
                .filter((c)->c.getDisabled().equals(false)).toList());
    }

    @PostMapping("/comment")
    public ResponseEntity<?> postComments(@RequestParam Long postId, String comment) {
        Post post = postRepository.findByIdAndDisabled(postId, false).orElseThrow(() ->
                new RuntimeException("Id is incorrect!")
        );
        post.getComments().add(
               commentRepository.save(Comment.builder()
                       .user(Util.getCurrentUser())
                       .content(comment)
                       .disabled(false)
                       .likes(0)
                       .build())
        );
        postRepository.save(post);
        return ResponseEntity.ok(post.getComments());
    }

    @DeleteMapping("/comment")
    public ResponseEntity<?> deleteComments(@RequestParam Long postId, @RequestParam Long commentId) {
        Post post = postRepository.findByIdAndDisabled(postId, false).orElseThrow();
        Comment comment = post.getComments().stream().filter((c) -> c.getId().equals(commentId)).findFirst().orElseThrow();
        if (!(comment.getUser().getId().equals(Util.getCurrentUser().getId()))) throw new RuntimeException("You cannot delete this comment!");
        comment.setDisabled(true);
        commentRepository.save(comment);
        return ResponseEntity.ok(post.getComments().stream()
                .filter((c)->c.getDisabled().equals(false)).toList());
    }
}


