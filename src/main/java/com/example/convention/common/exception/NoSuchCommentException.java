package com.example.convention.common.exception;

public class NoSuchCommentException extends BaseException {
    public NoSuchCommentException() {
        super("존재하지 않는 댓글입니다.");
    }
}
