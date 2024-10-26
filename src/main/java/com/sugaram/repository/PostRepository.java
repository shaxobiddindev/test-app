package com.sugaram.repository;

import com.sugaram.entity.Comment;
import com.sugaram.entity.Post;
import com.sugaram.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.parameters.P;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByDisabled(Boolean disabled, PageRequest pageRequest);
    Page<Post> findAllByAuthorAndDisabled(User author, Boolean disabled, PageRequest pageRequest);
    Optional<Post> findByIdAndDisabled(Long id, Boolean disabled);
    Optional<Post> findByIdAndAuthorAndDisabled(Long id, User author, Boolean disabled);

}
