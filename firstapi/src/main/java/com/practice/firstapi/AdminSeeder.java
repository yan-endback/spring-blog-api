package com.practice.firstapi;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminSeeder implements CommandLineRunner {
    private final AppUserRepository repo;
    private final PasswordEncoder encoder;

    public AdminSeeder(AppUserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }


    @Override public void run(String... args ){
        if (!repo.existsByUsername("admin"))
            repo.save(new AppUser("admin",encoder.encode("admin123"), Role.ADMIN));
    }
}
