package com.practice.firstapi;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
@RestController
public class PostController {
private final List<Post> postList = new ArrayList<>(List.of(
        new Post(1L, "первый пост", "тело", 1L),
        new Post(2L, "второй пост", "тело", 2L),
        new Post(3L, "третий пост", "тело", 3L)
    ));
    @GetMapping("/posts")
    public List<Post> all(){
        return postList;
    }
    @PostMapping("/posts")
    public ResponseEntity<Post> create(@RequestBody Post post){
        postList.add(post);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(post);
    }
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        postList.removeIf(p -> p.id().equals(id));
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/posts/1")
    public Post one (){
        return postList.get(0);
    }
    @GetMapping("/posts/{id}")
    public ResponseEntity<Post> byId(@PathVariable Long id){
        return postList.stream()
                .filter(e -> e.id().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    }
}
