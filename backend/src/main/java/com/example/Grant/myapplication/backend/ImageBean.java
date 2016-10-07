package com.example.Grant.myapplication.backend;

import java.sql.Blob;

/**
 * Created by Daniel on 10/6/2016.
 */

public class ImageBean {
    private Blob image;
    private String info;

    public Blob getData() {
        return image;
    }
    public String getInfo() { return info; }

    public void setData(Blob data) {
        image = data;
    }
    public void setInfo(String info) { info = info; }
}
