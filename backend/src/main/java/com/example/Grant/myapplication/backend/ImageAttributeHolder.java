package com.example.Grant.myapplication.backend;

/*
 * Used to store image attributes in one easy place
 */

class ImageAttributeHolder {
    private int ID;
    private double latitude;
    private double longitude;
    private String user;
    private String date;

    ImageAttributeHolder(int ID, double latitude, double longitude, String user, String date) {
        this.ID = ID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.user = user;
        this.date = date;
    }

    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public int getID() { return ID; }
    public String getUser() { return user; }
    public String getDate() { return date; }
}
