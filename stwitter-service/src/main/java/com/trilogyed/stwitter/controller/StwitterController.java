package com.trilogyed.stwitter.controller;

import com.trilogyed.stwitter.domain.Post;
import com.trilogyed.stwitter.service.ServiceLayer;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class StwitterController {

    public static final String EXCHANGE = "queue-demo-exchange";
    public static final String ROUTING_KEY = "comment.add.stwitter.controller";

    @Autowired
    RabbitTemplate rabbitTemplate;
    ServiceLayer service;

    public StwitterController(     RabbitTemplate rabbitTemplate, ServiceLayer service) {
        this.rabbitTemplate = rabbitTemplate;
        this.service = service;
    }

    @RequestMapping(value = "/posts", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public Post createPost(@RequestBody Post post) {
        return service.createPost(post);
    }

    @RequestMapping(value = "/posts/{id}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public Post getPost(@PathVariable int id) {
        return service.getPost(id);
    }
    @RequestMapping(value="/posts/{id}", method= RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public Post updatePost(@PathVariable int id, @RequestBody Post post) {
        post.setPostID(id);
        return service.updatePost(post);
    }

    @RequestMapping(value = "/posts/poster_name/{poster_name}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public List<Post> getPostsByPoster(@PathVariable String poster_name) {
        return service.getPostByPoster(poster_name);
    }
}


