package com.trilogyed.comment.controller;

import com.trilogyed.comment.InvalidIdException;
import com.trilogyed.comment.dao.CommentRepo;
import com.trilogyed.comment.dto.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class CommentController {

    @Autowired
    private CommentRepo repo;

    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public Comment createComment(@RequestBody Comment comment) {
        return repo.save(comment);
    }

    @RequestMapping(value="/comment/{comment_id}", method=RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public Comment getComment(@PathVariable int id) {
        Optional<Comment> comment = repo.findById(id);
        if (comment.isPresent() == false) {
            throw new InvalidIdException(id);
        }
        return comment.get();
    }

    @RequestMapping(value="/comment/{comment_id}", method=RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    public Comment updateComment(@PathVariable int id, @RequestBody Comment comment) {
        Comment checkVal = getComment(id);

        comment.setCommentId(id);

        return repo.save(comment);
    }

    @RequestMapping(value="/comment", method=RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public List<Comment> getAllComments() {
        return repo.findAll();
    }

    @RequestMapping(value="/comment/{comment_id}", method=RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable int id) {
        repo.deleteById(id);
    }

}
