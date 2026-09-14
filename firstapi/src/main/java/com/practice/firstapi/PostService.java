package com.practice.firstapi;

import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = auth.getName();
        System.out.println("Пост создает: " + currentUser);
        return mapper.toResponse(saved);
    }

    @Transactional
    public boolean delete(Long id){
        if (!repository.existsById(id)) return false;
        repository.deleteById(id); return true;
    }

    public PostResponse update(Long id,PostRequest request){
       Post post = repository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));
        post.setBody(request.body());
        post.setTitle(request.title());

        Author author = authorRepository.findById(request.authorId())
                .orElseThrow(()-> new AuthorNotFoundException(request.authorId()));
        post.setAuthor(author);
        Post saved = repository.save(post);
        return mapper.toResponse(saved);
    }
    public List<Post> findByUser(Long authorId){
        return repository.findByAuthorId(authorId);
    }
    public List<PostResponse> byAuthor(Long authorId){
        if (!authorRepository.existsById(authorId)){
            throw new AuthorNotFoundException(authorId);
        }
        List<Post> posts = repository.findByAuthorId(authorId);
        return posts.stream()
                .map(mapper::toResponse)
                .toList();
    }
}
