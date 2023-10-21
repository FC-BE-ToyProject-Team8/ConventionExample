package com.example.convention.controller.util;

import com.example.convention.controller.response.CommentResponse;
import com.example.convention.entity.Comment;
import java.util.List;

public class CommentDtoConverter {

    public static List<CommentResponse> toCommentResponses(List<Comment> comments) {
        return comments.stream()
            .map(CommentDtoConverter::toCommentResponse)
            .toList();
    }

    public static CommentResponse toCommentResponse(Comment comment) {
        return new CommentResponse(comment.getId(), comment.getComment());
    }
}
