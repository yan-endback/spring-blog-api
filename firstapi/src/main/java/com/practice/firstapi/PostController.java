package com.practice.firstapi;

import jakarta.validation.Valid;
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


    @GetMapping("/posts/{id}")
    public Post byId(@PathVariable Long id){
        return service.getById(id);
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
    @PostMapping("/posts")
    public ResponseEntity<Post> create(@Valid @RequestBody Post post){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(post));
    }
    @GetMapping ("/posts")
    public List<Post> findByUserId(@RequestParam (required = false) Long userId) {
        if (userId == null){
            return service.findAll();
    }
        return service.findByUser(userId);
    }
}
