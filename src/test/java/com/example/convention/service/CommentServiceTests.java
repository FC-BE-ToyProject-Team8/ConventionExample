package com.example.convention.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.convention.common.exception.NoSuchCommentException;
import com.example.convention.entity.Comment;
import com.example.convention.repository.CommentRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
@Transactional
public class CommentServiceTests {

    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    @BeforeEach
    void setUp() {
        commentService = new CommentService(commentRepository);
    }

    @Test
    void getComments() {
        // given
        Comment savedComment = commentRepository.save(new Comment("example"));
        Comment savedComment2 = commentRepository.save(new Comment("example2"));

        // when
        List<Comment> comments = commentService.getComments();

        // then
        assertThat(comments.size()).isEqualTo(2);
        assertThat(comments).contains(savedComment, savedComment2);
    }

    @Test
    void addComment() {
        // when
        Comment savedComment = commentService.addComment("example");

        // then
        List<Comment> comments = commentRepository.findAll();
        assertThat(comments.size()).isEqualTo(1);
        assertThat(comments).contains(savedComment);
    }

    @Test
    void editComment_정상_동작() {
        // given
        Comment savedComment = commentRepository.save(new Comment("example"));

        // when
        Comment editedComment = commentService.editComment(savedComment.getId(), "edited");

        // then
        assertThat(editedComment.getComment()).isEqualTo("edited");

        Comment commentFromRepository = commentRepository.findById(savedComment.getId()).orElseThrow();
        assertThat(editedComment).isEqualTo(commentFromRepository);
    }

    @Test
    void editComment_없는_댓글을_수정하려하면_예외() {
        // when, then
        assertThatThrownBy(() -> {
            commentService.editComment(0L, "edited");
        }).isInstanceOf(NoSuchCommentException.class);
    }

    @Test
    void deleteComment_정상_동작() {
        // given
        Comment savedComment = commentRepository.save(new Comment("example"));

        // when
        commentService.deleteComment(savedComment.getId());

        // then
        List<Comment> comments = commentRepository.findAll();
        assertThat(comments).isEmpty();
    }
}
