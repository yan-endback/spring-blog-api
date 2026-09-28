package com.practice.firstapi;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true)
    private String token;

    @ManyToOne(optional = false)
    @JoinColumn(name = "users_id")
    private AppUser user;

    @Column(nullable = false)
    private Instant expiresAt;

    protected RefreshToken () {}
    public RefreshToken(String token, AppUser user, Instant expiresAt) {
        this.token = token;
        this.user = user;
        this.expiresAt = expiresAt;
    }
    public boolean isExpired(){
        return Instant.now().isAfter(expiresAt);

    }
    public String getToken() { return token; }
    public AppUser getUser() { return user; }
    }
