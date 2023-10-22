package com.example.convention.controller;

import static com.example.convention.controller.util.CommentDtoConverter.toCommentResponse;
import static com.example.convention.controller.util.CommentDtoConverter.toCommentResponses;

import com.example.convention.common.response.BaseResponseBody;
import com.example.convention.common.response.DataResponseBody;
import com.example.convention.controller.request.CommentRequest;
import com.example.convention.controller.response.CommentResponse;
import com.example.convention.entity.Comment;
import com.example.convention.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Comment", description = "댓글 API")
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    @Operation(summary = "댓글 목록")
    public DataResponseBody<List<CommentResponse>> getComme록ts() {
        List<Comment> comments = commentService.getComments();
        List<CommentResponse> commentResponses = toCommentResponses(comments);
        return DataResponseBody.ok(commentResponses);
    }

    @PostMapping
    @Operation(summary = "댓글 작성")
    public DataResponseBody<CommentResponse> addComment(
        @Valid @RequestBody CommentRequest commentRequest
    ) {
        Comment comment = commentService.addComment(commentRequest.comment());
        CommentResponse commentResponse = toCommentResponse(comment);
        return DataResponseBody.ok(commentResponse);
    }

    @PutMapping("/{id}")
    @Operation(summary = "댓글 수정")
    public DataResponseBody<CommentResponse> editComment(
        @PathVariable Long id,
        @Valid @RequestBody CommentRequest commentRequest
    ) {
        Comment comment = commentService.editComment(id, commentRequest.comment());
        CommentResponse commentResponse = toCommentResponse(comment);
        return DataResponseBody.ok(commentResponse);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "댓글 삭제")
    public BaseResponseBody deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return BaseResponseBody.ok();
    }
}
