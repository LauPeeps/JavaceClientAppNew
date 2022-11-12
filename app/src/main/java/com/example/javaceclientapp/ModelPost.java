package com.example.javaceclientapp;

public class ModelPost {

    public ModelPost() {
    }

    String name, dp, uid, email, title, description, userImage, timePosted, likes, comments;


    public ModelPost(String name, String dp, String uid, String email, String title, String description, String userImage, String timePosted, String likes, String comments) {
        this.name = name;
        this.dp = dp;
        this.uid = uid;
        this.email = email;
        this.title = title;
        this.description = description;
        this.userImage = userImage;
        this.timePosted = timePosted;
        this.likes = likes;
        this.comments = comments;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getTimePosted() {
        return timePosted;
    }

    public void setTimePosted(String timePosted) {
        this.timePosted = timePosted;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
