package com.practice.firstapi;

import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import org.springframework.security.access.AccessDeniedException;
import java.util.List;

@Service
public class PostService {
    private final PostRepository repository;
    private final AuthorRepository authorRepository;
    private final PostMapper mapper;
    private final AppUserRepository userRepo;
    public PostService(PostRepository r, AuthorRepository ar, PostMapper m, AppUserRepository apr){
        this.repository=r;
        this.mapper=m;
        this.authorRepository=ar;
        this.userRepo=apr;
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

   public PostResponse create (PostRequest request, String username) {
       Author author = authorRepository.findById(request.authorId())
               .orElseThrow(() -> new AuthorNotFoundException(request.authorId()));
       AppUser owner = userRepo.findByUsername(username)
               .orElseThrow(() -> new UsernameNotFoundException(username));
       Post post = mapper.toEntity(request, author);
       post.setOwner(owner);
       return mapper.toResponse(repository.save(post));
   }

    @Transactional
    public void delete(Long id){
        Post post = repository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));
        checkCanModify(post);
        repository.delete(post);
    }

    private void checkCanModify(Post post)  {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = auth.getName();

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) return;
        if (post.getOwner() == null){
            throw new AccessDeniedException("У поста нет владельца — изменить может только администратор");
        }
        if (!post.getOwner().getUsername().equals(currentUser))
            throw new AccessDeniedException("Это не ваш пост");
    }
    public PostResponse update(Long id,PostRequest request){
        Post post = repository.findById(id)
                .orElseThrow(()-> new PostNotFoundException(id));

        checkCanModify(post);

        post.setTitle(request.title());
        post.setBody(request.body());

        return mapper.toResponse(repository.save(post));
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

    public void deleteAnyPost(Long id) {
        Post post = repository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));
        repository.delete(post);
    }
    public List<PostResponse> findMyPosts(String username){
        return repository.findByOwnerUsername(username)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }
}
