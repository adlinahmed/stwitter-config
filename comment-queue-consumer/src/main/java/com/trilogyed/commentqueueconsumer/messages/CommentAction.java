package com.trilogyed.commentqueueconsumer.messages;

import java.util.Objects;

public class CommentAction {

    private Comment comment;
    private String action;

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentAction that = (CommentAction) o;
        return Objects.equals(comment, that.comment) && Objects.equals(action, that.action);
    }

    @Override
    public int hashCode() {
        return Objects.hash(comment, action);
    }

    @Override
    public String toString() {
        return "CommentAction{" +
                "comment=" + comment +
                ", action='" + action + '\'' +
                '}';
    }
}
