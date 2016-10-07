package cs490.frame;

import java.io.Serializable;

/**
 * Created by Scott on 10/6/2016.
 */

public class Post implements Serializable{
    String picture;
    String user;
    double lat;
    double lng;

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
}
