package com.example.Grant.myapplication.backend;


import java.sql.Blob;

/**
 * Created by Grant on 11/2/2016.
 */

public class VideoBean {
    private Blob video;
    private String info;

    public Blob getData() {
        return video;
    }
    public String getInfo() { return info; }

    public void setData(Blob data) {
        video = data;
    }
    public void setInfo(String info) { info = info; }
}
