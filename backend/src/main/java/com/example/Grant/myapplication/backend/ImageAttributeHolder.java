package com.example.Grant.myapplication.backend;

/*
 * Used to store image attributes in one easy place
 */

class ImageAttributeHolder {
    private int ID;
    private int isVideo;
    private double latitude;
    private double longitude;
    private String user;
    private int votes;
    private String blobkey;
    private String date;
    private String caption;

    ImageAttributeHolder(int ID, int isVideo, double latitude, double longitude, String user, int votes, String blobkey, String date, String Caption) {
        this.ID = ID;
        this.isVideo = isVideo;
        this.latitude = latitude;
        this.longitude = longitude;
        this.user = user;
        this.votes = votes;
        this.blobkey = blobkey;
        this.date = date;

        this.caption = Caption;
    }

    public int getID() { return ID; }
    public int getIsVideo() { return isVideo; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public String getUser() { return user; }
    public int getVotes() { return votes; }
    public String getBlobkey() { return blobkey; }
    public String getDate() { return date; }
    public String getCaption() { return caption; }

}
