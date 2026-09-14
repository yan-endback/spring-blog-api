package com.practice.firstapi;

import com.practice.firstapi.AppUser;
import com.practice.firstapi.AppUserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService{
    private final AppUserRepository repo;
    CustomUserDetailsService(AppUserRepository repo){
        this.repo=repo;
    }
    @Override
    public UserDetails loadUserByUsername(String username){
        AppUser u = repo.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("Несуществующий пользователь: " + username));
        return User.withUsername(u.getUsername())
                .password(u.getPasswordHash())
                .roles(u.getRole().name())
                .build();
    }
}