package com.example.Grant.myapplication.backend;

import java.util.ArrayList;

/**
 * Created by Daniel on 10/6/2016.
 */

public class IAHBean {
    private ArrayList<SQLStatements.ImageAttributeHolder> iah;
    private String info;

    public ArrayList<SQLStatements.ImageAttributeHolder> getData() {
        return iah;
    }
    public String getInfo() { return info; }

    public void setData(ArrayList<SQLStatements.ImageAttributeHolder> iah) {
        iah = iah;
    }
    public void setInfo(String info) { info = info; }
}
