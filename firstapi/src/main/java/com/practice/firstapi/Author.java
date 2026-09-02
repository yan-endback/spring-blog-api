package com.practice.firstapi;

import jakarta.persistence.*;

@Entity
@Table(name = "author")
public class Author {
    private String name;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    protected Author (){}
    public Author(String name){this.name=name;}
    public String getName (){return name;}
    public Long getId(){return id;}
}
