package com.example.convention.repository;

import com.example.convention.entity.Comment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAll();

    Optional<Comment> findById(Long id);

    void deleteById(Long id);

    <S extends Comment> S save(S entity);
}
