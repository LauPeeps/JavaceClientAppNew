package com.example.javaceclientapp;

public class ModelComment {

    public ModelComment() {}


    String commentId, timePosted, commenter, comment, uid, email, name;


    public ModelComment(String commentId, String timePosted, String commenter, String comment, String uid, String email, String name) {
        this.commentId = commentId;
        this.timePosted = timePosted;
        this.commenter = commenter;
        this.comment = comment;
        this.uid = uid;
        this.email = email;
        this.name = name;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getTimePosted() {
        return timePosted;
    }

    public void setTimePosted(String timePosted) {
        this.timePosted = timePosted;
    }

    public String getCommenter() {
        return commenter;
    }

    public void setCommenter(String commenter) {
        this.commenter = commenter;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
