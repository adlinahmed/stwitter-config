package com.trilogyed.commentqueueconsumer.feign;

import com.trilogyed.commentqueueconsumer.messages.Comment;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "comment-service")
public interface CommentService {
    @RequestMapping(value = "/comment", method= RequestMethod.POST)
    public Comment createComment(@RequestBody Comment comment);

    @RequestMapping(value = "/comment/{id}", method= RequestMethod.DELETE)
    public void deleteComment(@PathVariable int id);

}
