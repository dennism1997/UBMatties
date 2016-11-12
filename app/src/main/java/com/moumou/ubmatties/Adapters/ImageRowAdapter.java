package com.moumou.ubmatties.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.moumou.ubmatties.R;

import java.util.ArrayList;

/**
 * Created by MouMou on 01-11-16.
 */

public class ImageRowAdapter extends ArrayAdapter<Bitmap> {

    private ArrayList<Bitmap> imageList;

    public ImageRowAdapter(Context context, int resource, ArrayList<Bitmap> imageList) {
        super(context, resource, imageList);
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.image_row, parent, false);
        }

        ImageView imageView = (ImageView) view.findViewById(R.id.image_item);
        imageView.setImageBitmap(imageList.get(position));

        return view;
    }
}
