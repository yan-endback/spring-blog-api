package com.practice.firstapi;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/posts")
    public List<Post> all(){
        return service.findAll();
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<Post> byId(@PathVariable Long id){
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Void> delete(Long id){
       return service.delete(id)
               ? ResponseEntity.noContent().build()
               : ResponseEntity.notFound().build();
    }
    @PutMapping("/posts/{id}")
    public ResponseEntity<Post> update(@PathVariable Long id
    , @RequestBody Post post){
        return service.update(id,post)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
