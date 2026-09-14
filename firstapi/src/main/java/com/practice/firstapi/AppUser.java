package com.practice.firstapi;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class AppUser {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

@Column(nullable = false,unique = true)
    private String username;

@Column(nullable = false)
    private String passwordHash;

@Enumerated(EnumType.STRING)
    private Role role;
AppUser(String username,String passwordHash,Role role){
    this.username=username;
    this.passwordHash=passwordHash;
    this.role=role;

}
protected AppUser(){}
public String getUsername(){return username;}
public String getPasswordHash(){return passwordHash;}
public Role getRole(){return role;}
public Long getId(){return id;}

}

