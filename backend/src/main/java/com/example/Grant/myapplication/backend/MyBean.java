package com.example.Grant.myapplication.backend;

import com.google.appengine.api.blobstore.BlobKey;

/**
 * The object model for the data we are sending through endpoints
 */
public class MyBean {

    private boolean myData;
    private String info;
    private BlobKey blobKey;
    private int postID;

    public boolean getData() {
        return myData;
    }
    public String getInfo() { return info; }

    public void setData(boolean data) {
        myData = data;
    }
    public void setInfo(String info1) { info = info1; }

    public BlobKey getBlobKey() {
        return blobKey;
    }
    public void setBlobKey(BlobKey blobKey) {
        this.blobKey = blobKey;
    }

    public int getPostID() {
        return postID;
    }
    public void setPostID(int postID) {
        this.postID = postID;
    }
}