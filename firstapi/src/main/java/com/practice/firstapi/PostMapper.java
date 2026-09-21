package com.practice.firstapi;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {
    public PostResponse toResponse(Post post){
        return new PostResponse(
                  post.getId()
                , post.getTitle()
                , post.getBody()
                , post.getAuthor().getName()
                , post.getCreatedAt()
                , post.getOwnerUsername()
        );
    }
    public Post toEntity(PostRequest request, Author author){
        return new Post(request.title(),request.body(),author);
    }
}
