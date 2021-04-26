package com.trilogyed.stwitter.util.feign;

import com.trilogyed.stwitter.domain.Post;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name = "post")
public
interface PostClient {

    @RequestMapping(value = "/posts", method = RequestMethod.POST)
    Post createPost(@RequestBody Post post);

    @RequestMapping(value = "/posts/{id}", method = RequestMethod.GET)
    Post getPost(@PathVariable int id);

    @RequestMapping(value = "/posts/{id}", method = RequestMethod.PUT)
    Post updatePost(@PathVariable int id, Post post);

    @RequestMapping(value = "/posts/poster_name/{poster_name}", method = RequestMethod.GET)
    List<Post> getPostsByPoster(@PathVariable String poster_name);
}
