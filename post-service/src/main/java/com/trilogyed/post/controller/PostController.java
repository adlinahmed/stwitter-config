package com.trilogyed.post.controller;

import com.trilogyed.post.InvalidIdException;
import com.trilogyed.post.dao.PostRepo;
import com.trilogyed.post.dto.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class PostController {

    @Autowired
    private PostRepo repo;


    @RequestMapping(value = "/posts", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public Post createPost(@RequestBody Post post) {
        return repo.save(post);
    }

    @RequestMapping(value = "/posts/{id}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public Post getPost(@PathVariable int id) {
        Optional<Post> post = repo.findById(id);
        if (post.isPresent() == false) {
            throw new InvalidIdException(id);
        }
        return post.get();
    }

    @RequestMapping(value="/posts", method=RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public List<Post> getAllPosts() {
        return repo.findAll();
    }

    @RequestMapping(value = "/posts/{id}", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    public void updatePost(@RequestBody Post post, @PathVariable int id) {
        repo.save(post);
    }

    @RequestMapping(value = "/posts/{id}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable int id) {
        repo.deleteById(id);
    }

    @RequestMapping(value = "/posts/poster_name/{poster_name}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public List<Post> getPostsByPoster(@PathVariable String poster_name) {
        return repo.findPostByPoster(poster_name);
    }
}
