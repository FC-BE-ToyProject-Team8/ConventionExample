package com.example.convention.controller.request;

import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

public record CommentRequest(
    @NotBlank
    String comment
) {

}
