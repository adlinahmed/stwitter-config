package com.trilogyed.post.dao;

import com.trilogyed.post.dto.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepo extends JpaRepository<Post, Integer> {
    List<Post> findPostByPoster(String posterName);
}
