package com.practice.firstapi;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    private final PostRepository repository;
    public PostService(PostRepository r){ this.repository=r;}

    public List<Post> findAll() {return repository.findAll();}

    public Post getById(Long id){
        return repository.findById(id)
                .orElseThrow(()-> new PostNotFoundException(id));
    }

    public Post create(Post post) {return repository.save(post);}
    @Transactional
    public boolean delete(Long id){
        if (!repository.existsById(id)) return false;
        repository.deleteById(id); return true;
    }

    public Optional<Post> update(Long id,Post newPost){
        return repository.findById(id)
                .map(existing -> {
                    existing.setTitle(newPost.getTitle());
                    existing.setBody(newPost.getBody());
                    existing.setAuthor(newPost.getAuthor());
                    return repository.save(existing);
                });
    }
    public List<Post> findByUser(Long authorId){
        return repository.findByAuthorId(authorId);
    }
}
