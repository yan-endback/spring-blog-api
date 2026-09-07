package com.practice.firstapi;

import java.time.LocalDateTime;

public record PostResponse(
     Long id
   , String title
   , String body
   , String authorName
   , LocalDateTime createdAt
) {}
