package com.example.Grant.myapplication.backend;

/**
 * The object model for the data we are sending through endpoints
 */
public class MyBean {

    private boolean myData;
    private String info;
    int postID;

    public boolean getData() {
        return myData;
    }
    public String getInfo() { return info; }

    public void setData(boolean data) {
        myData = data;
    }
    public void setInfo(String info1) { info = info1; }

    public int getPostID() {
        return postID;
    }
    public void setPostID(int postID) {
        this.postID = postID;
    }
}