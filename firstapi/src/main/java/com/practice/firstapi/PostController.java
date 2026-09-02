package com.practice.firstapi;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
@RestController
public class PostController {

    private final PostService service;

    public PostController(PostService service){
        this.service=service;
        System.out.println("создан КОНТРОЛЛЕР");
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<PostResponse> byId(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
       return service.delete(id)
               ? ResponseEntity.noContent().build()
                   : ResponseEntity.notFound().build();
        }
    @PutMapping("/posts/{id}")
    public ResponseEntity<Post> update( @PathVariable Long id
    ,@Valid @RequestBody Post post){
        return service.update(id,post)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping ("/posts")
    public List<PostResponse> all(){
        return service.findAll();
    }
    @PostMapping("/posts")
    public ResponseEntity<PostResponse> create(@Valid @RequestBody PostRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(request));
    }
}
