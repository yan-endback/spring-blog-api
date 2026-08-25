package com.practice.firstapi;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

public record Post(
          Long id
        , @NotBlank(message = "title не должен быть пустым") String title
        , @NotBlank(message = "body не должен быть пустым") String body
        , @NotNull(message = "userId обязателен") Long userId) {}
