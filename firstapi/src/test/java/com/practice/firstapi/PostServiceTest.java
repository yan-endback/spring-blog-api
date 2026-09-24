package com.practice.firstapi;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock PostRepository postRepository;
    @Mock AuthorRepository authorRepository;
    @Mock PostMapper postMapper;
    @Mock AppUserRepository appUserRepository;

    @InjectMocks PostService postService;

    @Test
    void getById_missing_throwsNotFound() {
        when(postRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(PostNotFoundException.class, () -> postService.getById(99L));
    }
    @AfterEach
    void clearSecurity(){
        SecurityContextHolder.clearContext();
    }
    private void loginAs(String username,String role){
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        username,null,
                        List.of(
                                new SimpleGrantedAuthority("ROLE_" + role))));
    }
    private Post postOwnedBy(String username){
        AppUser owner = new AppUser();
                owner.setUsername(username);
        Post post = new Post();
        post.setId(8L);
        post.setOwner(owner);
        return post;
    }
    @Test
    void delete_notOwner_throwsAccessDenied() {
        loginAs("second", "USER");
        when(postRepository.findById(8L)).thenReturn(Optional.of(postOwnedBy("murder")));

        assertThrows(AccessDeniedException.class, () -> postService.delete(8L));
        verify(postRepository, never()).delete(any());
    }
    @Test
    void delete_owner_deletes(){
        loginAs("murder","USER");
        Post post = postOwnedBy("murder");
        when(postRepository.findById(8L)).thenReturn(Optional.of(post));

        postService.delete(8L);

        verify(postRepository).delete(post);
    }
    @Test
    void delete_admin_deleteAnyPost(){
        loginAs("boss","ADMIN");
        Post post = postOwnedBy("murder");
        when(postRepository.findById(8L)).thenReturn(Optional.of(post));
        postService.delete(8L);
        verify(postRepository).delete(post);
    }
}
