package com.moumou.ubmatties.Adapters;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.moumou.ubmatties.R;

import java.util.ArrayList;

/**
 * Created by MouMou on 01-11-16.
 */

public class ImageRowAdapter extends RecyclerView.Adapter<ImageRowAdapter.ViewHolder> {

    private ArrayList<Bitmap> imageList;

    public ImageRowAdapter(ArrayList<Bitmap> list) {
        this.imageList = list;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        // each data item is just a string in this case
        public ImageView imageView;

        public ViewHolder(View v) {
            super(v);
            imageView = (ImageView) v.findViewById(R.id.image_item);
        }
    }

    @Override
    public ImageRowAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_row, parent, false);

        return new ImageRowAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ImageRowAdapter.ViewHolder holder, int position) {
        holder.imageView.setImageBitmap(imageList.get(position));
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }
}
