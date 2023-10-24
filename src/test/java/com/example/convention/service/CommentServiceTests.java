package com.example.convention.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.example.convention.common.exception.NoSuchCommentException;
import com.example.convention.entity.Comment;
import com.example.convention.repository.CommentRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTests {

    @InjectMocks
    private CommentService commentService;

    @Mock
    private CommentRepository commentRepository;

    @Test
    void editComment_정상_동작() {
        // given
        Long commentId = 1L;
        Comment comment = new Comment("example");
        given(commentRepository.findById(commentId)) // comemntId에 대해 findById가 호출되면
            .willReturn(Optional.of(comment)); // Optional.of(comment)를 반환하도록
        given(commentRepository.save(any())) // 어떤 매개변수든 save가 호출되면
            .willAnswer(invocation -> invocation.getArguments()[0]); // 첫번째 매개변수를 그대로 반환하도록

        // when
        Comment editedComment = commentService.editComment(1L, "edited");

        // then
        assertThat(editedComment.getComment()).isEqualTo("edited");
    }

    @Test
    void editComment_없는_댓글을_수정하려하면_예외() {
        // given
        given(commentRepository.findById(any()))
            .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> {
            commentService.editComment(0L, "edited");
        }).isInstanceOf(NoSuchCommentException.class);
    }
}
