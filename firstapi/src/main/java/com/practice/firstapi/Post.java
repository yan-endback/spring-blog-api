package com.practice.firstapi;

import jakarta.persistence.*;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
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
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Author author;

    protected Post() {}

    public Post (String title,String body,Author author){
        this.body=body;
        this.title=title;
        this.author=author;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getBody() { return body; }
    public Author getAuthor() { return author; }
    public LocalDateTime getCreatedAt () { return createdAt;}
    public void setTitle(String t) { this.title = t; }
    public void setBody(String b) { this.body = b; }
    public void setAuthor(Author author) { this.author = author; }
}
