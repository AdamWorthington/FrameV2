package com.example.Grant.myapplication.backend;

import java.util.ArrayList;

/**
 * Created by Daniel on 10/6/2016.
 */

public class IAHBean {
    private ArrayList<ImageAttributeHolder> iah;
    private String info;

    public ArrayList<ImageAttributeHolder> getList() {
        return iah;
    }
    public String getInfo() { return info; }

    public void setData(ArrayList<ImageAttributeHolder> iah) {
        iah = iah;
    }
    public void setInfo(String info) { info = info; }
}
