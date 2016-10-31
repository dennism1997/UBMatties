package com.moumou.ubmatties;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Observable;

/**
 * Created by MouMou on 04-10-16
 */

public class User extends Observable {

    private String name;
    private Bitmap image;
    private String id;
    private String imageURL;

    public User(String name, String id, String url) {
        this.name = name;
        this.id = id;
        try {
            URL link = new URL(url);
            ImageFromInternet imgGetter = new ImageFromInternet();
            imgGetter.execute(link);
        } catch (MalformedURLException e) {
            image = null;
            e.printStackTrace();
        }
        this.imageURL = url;
    }

    public User(String name, String id) {
        this.name = name;
        this.id = id;
        image = null;

    }

    public String getName() {
        return name;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String url) {
        try {
            URL link = new URL(url);
            ImageFromInternet imgGetter = new ImageFromInternet();
            imgGetter.execute(link);
        } catch (MalformedURLException e) {
            image = null;
            e.printStackTrace();
        }
    }

    private class ImageFromInternet extends AsyncTask<URL, Void, Bitmap> {
        Bitmap result;

        @Override
        protected Bitmap doInBackground(URL... params) {
            try {
                result = BitmapFactory.decodeStream(params[0].openConnection().getInputStream());
                return result;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            User.this.setImage(bitmap);
            User.this.setChanged();
            notifyObservers();
        }
    }

    public String getId() {
        return this.id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User) {
            User other = (User) obj;
            return this.getName().equals(other.getName()) && this.getId().equals(other.getId());
        }
        return false;
    }

    public String getImageURL() {
        return imageURL;
    }
}
