package com.practice.firstapi;

import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.access.AccessDeniedException;

import org.springframework.data.domain.Pageable;
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
    public PageResponse<PostResponse> all(
            @PageableDefault(
                    size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        return PageResponse.from(service.findAll(pageable));
    }
    @PostMapping("/posts")
    public ResponseEntity<PostResponse> create(@Valid @RequestBody PostRequest request ,@AuthenticationPrincipal UserDetails user){
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
