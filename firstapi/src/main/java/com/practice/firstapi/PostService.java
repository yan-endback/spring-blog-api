package com.practice.firstapi;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    private final PostRepository repository;
    private final AuthorRepository authorRepository;
    private final PostMapper mapper;

    public PostService(PostRepository r,AuthorRepository ar,PostMapper m){
        this.repository=r;
        this.mapper=m;
        this.authorRepository=ar;
    }

    public List<PostResponse> findAll() {
        return repository.findAllWithAuthors().stream()
                .map(mapper::toResponse)
                .toList();
    }

    public PostResponse getById(Long id){
        Post post = repository.findById(id)
                .orElseThrow(()-> new PostNotFoundException(id));
        return mapper.toResponse(post);
    }

    public PostResponse create(PostRequest request) {
        Author author = authorRepository.findById(request.authorId())
                .orElseThrow(() -> new AuthorNotFoundException(request.authorId()));
        Post saved = repository.save(mapper.toEntity(request,author));
        return mapper.toResponse(saved);
    }

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
