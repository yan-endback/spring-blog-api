package com.practice.firstapi;

public record PostResponse(
     Long id
   , String title
   , String body
   , String authorName
) {}
