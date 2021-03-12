package com.commax.ocf.server.app.recyclerview;

public class Recycler_item {

    int image;
    String title;

    public int getImage() {
        return image;
    }
    public String getTitle() {
        return title;
    }

    public Recycler_item(int image, String title) {
        this.image = image;
        this.title = title;
    }
}
