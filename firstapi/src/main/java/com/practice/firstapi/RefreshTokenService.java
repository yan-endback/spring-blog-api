package com.practice.firstapi;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private static final Duration REFRESH_TTL = Duration.ofDays(7);

    private final RefreshTokenRepository repository;
    private final AppUserRepository userRepository;

    public RefreshTokenService(RefreshTokenRepository repository, AppUserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public RefreshToken create(String username){
        AppUser user = userRepository.findByUsername(username)
                .orElseThrow();
        return issue(user);
    }

    @Transactional
    public RefreshToken rotate(String value){
        RefreshToken old = repository.findByToken(value)
                .orElseThrow(() ->
                        new InvalidRefreshTokenException("Refresh-токен не найден,либо уже существует"));
        if (old.isExpired()){
            throw new InvalidRefreshTokenException("Срок refresh-токена истек,войди заново");
        }
        repository.delete(old);
        return issue(old.getUser());
    }

    @Transactional
    public void revoke(String value){
         repository.findByToken(value).ifPresent(repository::delete);
    }

    public RefreshToken issue(AppUser user){
        String value = UUID.randomUUID().toString();
        return repository.save(new RefreshToken(
                value,user, Instant.now().plus(REFRESH_TTL)));
    }
}
