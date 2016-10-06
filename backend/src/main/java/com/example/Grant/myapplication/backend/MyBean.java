package com.example.Grant.myapplication.backend;

/**
 * The object model for the data we are sending through endpoints
 */
public class MyBean {

    private boolean myData;

    public boolean getData() {
        return myData;
    }

    public void setData(boolean data) {
        myData = data;
    }
}