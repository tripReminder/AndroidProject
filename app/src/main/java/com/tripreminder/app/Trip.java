package com.tripreminder.app;

public class Trip {

    private String title;
    private int statusImg;
    private int tripPic;


    public Trip(String title, int statusImg, int tripPic) {
        this.title = title;
        this.statusImg = statusImg;
        this.tripPic = tripPic;
    }

    public String getTitle() {
        return title;
    }

    public int getStatusImg() {
        return statusImg;
    }

    public int getTripPic() {
        return tripPic;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setStatusImg(int statusImg) {
        this.statusImg = statusImg;
    }

    public void setTripPic(int tripPic) {
        this.tripPic = tripPic;
    }
}
