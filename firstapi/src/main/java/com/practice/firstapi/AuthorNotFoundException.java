package com.practice.firstapi;

public class AuthorNotFoundException extends RuntimeException{
    public AuthorNotFoundException(Long id){
        super ("Автор " + id + " не найден" );
    }
}
