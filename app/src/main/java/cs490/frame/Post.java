package cs490.frame;

import java.io.Serializable;

/**
 * Created by Scott on 10/6/2016.
 */

public class Post implements Serializable{
    String picture;
    String user;
    String caption;
    int likes;
    int postID;
    double lat;
    double lng;
    byte[] video;

    public byte[] getVideo() {
        return video;
    }

    public void setVideo(byte[] video) {
        this.video = video;
    }

    public String getPicture() {
        return picture;
    }

    public String getUser() {
        return user;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getPostID() {
        return postID;
    }

    public void setPostID(int postID) {
        this.postID = postID;
    }
}
