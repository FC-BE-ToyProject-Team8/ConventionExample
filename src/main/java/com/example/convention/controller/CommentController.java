package com.example.convention.controller;

import static com.example.convention.controller.util.CommentDtoConverter.toCommentResponse;
import static com.example.convention.controller.util.CommentDtoConverter.toCommentResponses;

import com.example.convention.common.response.BaseResponseBody;
import com.example.convention.common.response.DataResponseBody;
import com.example.convention.controller.request.CommentRequest;
import com.example.convention.controller.response.CommentResponse;
import com.example.convention.entity.Comment;
import com.example.convention.service.CommentService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    public DataResponseBody<List<CommentResponse>> getComments() {
        List<Comment> comments = commentService.getComments();
        List<CommentResponse> commentResponses = toCommentResponses(comments);
        return DataResponseBody.ok(commentResponses);
    }

    @PostMapping
    public DataResponseBody<CommentResponse> addComment(
        @Valid @RequestBody CommentRequest commentRequest
    ) {
        Comment comment = commentService.addComment(commentRequest.comment());
        CommentResponse commentResponse = toCommentResponse(comment);
        return DataResponseBody.ok(commentResponse);
    }

    @PutMapping("/{id}")
    public DataResponseBody<CommentResponse> editComment(
        @PathVariable Long id,
        @Valid @RequestBody CommentRequest commentRequest
    ) {
        Comment comment = commentService.editComment(id, commentRequest.comment());
        CommentResponse commentResponse = toCommentResponse(comment);
        return DataResponseBody.ok(commentResponse);
    }

    @DeleteMapping("/{id}")
    public BaseResponseBody deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return BaseResponseBody.ok();
    }
}
