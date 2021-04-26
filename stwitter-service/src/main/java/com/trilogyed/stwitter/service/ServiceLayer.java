package com.trilogyed.stwitter.service;

import com.trilogyed.stwitter.domain.Post;
import com.trilogyed.stwitter.exception.NoSuchPostException;
import com.trilogyed.stwitter.util.feign.CommentClient;
import com.trilogyed.stwitter.util.feign.PostClient;
import feign.FeignException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceLayer {

    private PostClient postClient;
    private CommentClient commentClient;
    private RabbitTemplate rabbitTemplate;

    @Autowired
    public ServiceLayer(PostClient postClient) {
        this.postClient = postClient;
    }

    public Post createPost(Post post) {
        return postClient.createPost(post);
    }

    public Post getPost(int id) {
        return postClient.getPost(id);
    }

    public Post updatePost(Post post) throws NoSuchPostException {
        Post postToUpdate;
        try {
            postToUpdate = postClient.getPost(post.getPostID());
        } catch (FeignException ex) {
            throw new NoSuchPostException("There is no post with id " + post.getPostID());
        }
        postToUpdate.setPost(post.getPost());
        return postClient.updatePost(postToUpdate.getPostID(), postToUpdate);
    }

    public List<Post> getPostByPoster(String poster_name) {
        return postClient.getPostsByPoster(poster_name);
    }

}
