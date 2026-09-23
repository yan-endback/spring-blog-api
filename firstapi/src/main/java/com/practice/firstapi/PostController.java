package com.practice.firstapi;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.access.AccessDeniedException;
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
    public ResponseEntity<Void> delete(@PathVariable Long id)  {
        service.delete(id);
    return ResponseEntity.noContent().build();
    }
    @PutMapping("/posts/{id}")
    public PostResponse update(@PathVariable Long id,
           @Valid @RequestBody PostRequest request)  {
        return service.update(id,request);
    }
    @GetMapping ("/posts")
    public List<PostResponse> all(){
        return service.findAll();
    }
    @PostMapping("/posts")
    public ResponseEntity<PostResponse> create(@RequestBody PostRequest request ,@AuthenticationPrincipal UserDetails user){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(request,user.getUsername()));
    }
    @GetMapping ("/authors/{id}/posts")
    public List<PostResponse> byAuthor(
            @PathVariable Long id
    ){
        return service.byAuthor(id);
    }
    @DeleteMapping("/admin/posts/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteByAdmin(@PathVariable Long id){
        service.deleteAnyPost(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping ("/posts/my")
    public List<PostResponse> myPosts(@AuthenticationPrincipal UserDetails user){
        return service.findMyPosts(user.getUsername());
    }
}
