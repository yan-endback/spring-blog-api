package com.practice.firstapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository repository;
    private final AppUserRepository userRepository;
    private final AuthProperties props;
    private static final Logger log = LoggerFactory.getLogger(RefreshTokenService.class);
    public RefreshTokenService(RefreshTokenRepository repository, AppUserRepository userRepository,AuthProperties props) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.props = props;
    }

    public RefreshToken create(String username){
        AppUser user = userRepository.findByUsername(username)
                .orElseThrow();
        return issue(user);
    }

    @Transactional
    public RefreshToken rotate(String value){
        RefreshToken old = repository.findByToken(value)
                .orElseThrow(() -> {
                    log.warn("Обмен refresh отклонён: токен не найден или уже использован");
                    return new InvalidRefreshTokenException("Refresh-токен не найден или уже использован");
                });
        if (old.isExpired()){
            log.warn("Обмен refresh отклонён: токен истёк, userId={}", old.getUser().getId());
            throw new InvalidRefreshTokenException("Срок refresh-токена истек,войди заново");
        }
        repository.delete(old);
        log.debug("Refresh обменян для пользователя {}", old.getUser().getUsername());
        return issue(old.getUser());
    }

    @Transactional
    public void revoke(String value){
         repository.findByToken(value).ifPresent(repository::delete);
    }

    public RefreshToken issue(AppUser user){
        String value = UUID.randomUUID().toString();
        return repository.save(new RefreshToken(
                value,user, Instant.now().plus(props.accessTt1())));
    }
}
