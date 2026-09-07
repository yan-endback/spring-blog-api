package com.practice.firstapi;

public class PostNotFoundException extends RuntimeException{
    public PostNotFoundException(Long id){

        super ("Пост " + id + " не найден");
    }
    
}
