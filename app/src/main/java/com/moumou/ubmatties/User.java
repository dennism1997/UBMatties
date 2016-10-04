package com.moumou.ubmatties;

import android.media.Image;

/**
 * Created by MouMou on 04-10-16.
 */

public class User {

    private String name;
    private Image image;

    public User(String name, Image image) {
        this.name = name;
        this.image = image;
    }

    public User(String name) {
        this.name = name;
        this.image = null;
    }

    public String getName() {
        return name;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }
}
