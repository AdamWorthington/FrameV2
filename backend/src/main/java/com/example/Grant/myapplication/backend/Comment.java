package com.example.Grant.myapplication.backend;

/**
 * Created by Daniel on 12/5/2016.
 */

public class Comment {

    int postID;
    String comment;
    String user;

    public Comment (int postID, String comment, String user) {
        this.postID = postID;
        this.comment = comment;
        this.user = user;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getPostID() {
        return postID;
    }

    public void setPostID(int postID) {
        this.postID = postID;
    }
}
