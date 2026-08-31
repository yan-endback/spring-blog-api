package com.practice.firstapi;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String body;
    private Long userId;

    protected Post() {}

    public Post (String title,String body,Long userId){
        this.body=body;
        this.userId=userId;
        this.title=title;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getBody() { return body; }
    public Long getUserId() { return userId; }
    public void setTitle(String t) { this.title = t; }
    public void setBody(String b) { this.body = b; }
    public void setUserId(Long u) { this.userId = u; }
}
