package com.practice.firstapi;

import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @Size(min = 3,max = 30, message = ("Логин: 3-30 символов"))
        String username,
        @Size(min = 6, message = ("Пароль: минимум 6 символов"))
        String password
) {}
