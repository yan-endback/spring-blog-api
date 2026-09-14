package com.practice.firstapi;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final AppUserRepository repo;
    private final PasswordEncoder encoder;

    public AuthService(AppUserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }
    public void register(String username,String rawPassword){
        if(repo.existsByUsername(username))
            throw new UserAlreadyExistsException(username);
        AppUser u = new AppUser(username,encoder.encode(rawPassword),Role.USER);
        repo.save(u);
    }
}
