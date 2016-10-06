package com.example.Grant.myapplication.backend;

/**
 * Created by Daniel on 10/6/2016.
 */

public class ImageBean {
    private String image;
    private String info;

    public String getData() {
        return image;
    }
    public String getInfo() { return info; }

    public void setData(String data) {
        image = data;
    }
    public void setInfo(String info) { info = info; }
}
