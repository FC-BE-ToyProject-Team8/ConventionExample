package com.example.convention.service;

import com.example.convention.common.exception.NoSuchCommentException;
import com.example.convention.entity.Comment;
import com.example.convention.repository.CommentRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public List<Comment> getComments() {
        return commentRepository.findAll();
    }

    public Comment addComment(String comment) {
        return commentRepository.save(new Comment(comment));
    }

    public Comment editComment(Long id, String comment) {
        Comment savedComment = commentRepository.findById(id)
            .orElseThrow(NoSuchCommentException::new);
        savedComment.editComment(comment);

        return commentRepository.save(savedComment);
    }

    public void deleteComment(Long id) {
        try {
            commentRepository.deleteById(id);
        } catch (IllegalArgumentException e) {
            throw new NoSuchCommentException();
        }
    }
}
