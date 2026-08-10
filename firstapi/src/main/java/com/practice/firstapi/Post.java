package com.practice.firstapi;

import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

public record Post(Long id, String title, String body, Long userId) {}
