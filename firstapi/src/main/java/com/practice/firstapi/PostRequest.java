package com.practice.firstapi;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PostRequest(
      @NotBlank(message = "title не должен быть пустым") String title
    , @NotBlank(message = "body не должен быть пустым") String body
    , @NotNull(message = "authorId обязателен") Long authorId
) {}
