package com.trilogyed.commentqueueconsumer;

import com.trilogyed.commentqueueconsumer.feign.CommentService;
import com.trilogyed.commentqueueconsumer.messages.CommentAction;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageListener {

    @Autowired
    CommentService commentService;

    @RabbitListener(queues = CommentQueueConsumerApplication.QUEUE_NAME)
    public void receiveMessage(CommentAction commentAction) {
        System.out.println("Received a message " + commentAction);
        if (commentAction.getAction().equals("DELETE")) {
            System.out.println("Deleting note");
            commentService.deleteComment(commentAction.getComment().getCommentId());
        } else {
            System.out.println("Adding note");
            commentService.createComment(commentAction.getComment());
        }
    }
}
