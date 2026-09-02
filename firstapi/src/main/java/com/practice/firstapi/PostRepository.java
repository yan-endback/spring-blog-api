package com.practice.firstapi;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
    List<Post> findByAuthorId(Long authorId);
@Query("SELECT p FROM Post p JOIN FETCH p.author")
    List<Post> findAllWithAuthors();

}
