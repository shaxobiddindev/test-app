package com.sugaram.repository;

import com.sugaram.entity.Comment;
import com.sugaram.entity.Post;
import com.sugaram.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByUser(User userId);
}
