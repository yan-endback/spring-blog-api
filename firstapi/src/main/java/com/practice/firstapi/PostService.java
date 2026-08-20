package com.practice.firstapi;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    private final List<Post> posts = new ArrayList<>(List.of(
            new Post(1L, "первый пост", "тело", 1L),
            new Post(2L, "второй пост", "тело", 2L),
            new Post(3L, "третий пост", "тело", 3L)
    ));

    public PostService() {
        System.out.println("создан СЕРВИС");

    }

    public List<Post> findAll(){ return posts; }

    public Optional<Post> findById(Long id) {
        return posts.stream()
                .filter(e -> e.id().equals(id))
                .findFirst();
    }

    public Post create(Post post) {posts.add(post); return post;}

    public boolean delete(Long id){
        return posts.removeIf(e -> e.id().equals(id));
    }

    public Optional<Post> update (Long id, Post newPost){
        boolean removed = posts.removeIf(p -> p.id().equals(id));
        if (!removed) return  Optional.empty();
        Post replaced = new Post(id,newPost.title(), newPost.body(), newPost.userId());
        posts.add(replaced);
        return Optional.of(replaced);
    }
}
