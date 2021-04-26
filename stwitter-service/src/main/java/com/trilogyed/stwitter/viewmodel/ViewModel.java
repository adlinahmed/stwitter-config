package com.trilogyed.stwitter.viewmodel;

import com.trilogyed.stwitter.domain.Comment;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class ViewModel {

    private int postID;
    private LocalDate postDate;
    private String posterName;
    private String post;
    private int commentId;
    private LocalDate createDate;
    private String commenterName;
    private String comment;
    private List<Comment> commentList;

    public int getPostID() {
        return postID;
    }

    public void setPostID(int postID) {
        this.postID = postID;
    }

    public LocalDate getPostDate() {
        return postDate;
    }

    public void setPostDate(LocalDate postDate) {
        this.postDate = postDate;
    }

    public String getPosterName() {
        return posterName;
    }

    public void setPosterName(String posterName) {
        this.posterName = posterName;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public String getCommenterName() {
        return commenterName;
    }

    public void setCommenterName(String commenterName) {
        this.commenterName = commenterName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ViewModel viewModel = (ViewModel) o;
        return postID == viewModel.postID && commentId == viewModel.commentId && Objects.equals(postDate, viewModel.postDate) && Objects.equals(posterName, viewModel.posterName) && Objects.equals(post, viewModel.post) && Objects.equals(createDate, viewModel.createDate) && Objects.equals(commenterName, viewModel.commenterName) && Objects.equals(comment, viewModel.comment) && Objects.equals(commentList, viewModel.commentList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postID, postDate, posterName, post, commentId, createDate, commenterName, comment, commentList);
    }

    @Override
    public String toString() {
        return "ViewModel{" +
                "postID=" + postID +
                ", postDate=" + postDate +
                ", posterName='" + posterName + '\'' +
                ", post='" + post + '\'' +
                ", commentId=" + commentId +
                ", createDate=" + createDate +
                ", commenterName='" + commenterName + '\'' +
                ", comment='" + comment + '\'' +
                ", commentList=" + commentList +
                '}';
    }
}
