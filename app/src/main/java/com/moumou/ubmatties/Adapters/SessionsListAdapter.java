package com.moumou.ubmatties.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.moumou.ubmatties.R;
import com.moumou.ubmatties.Session;
import com.moumou.ubmatties.User;
import com.moumou.ubmatties.globals.SessionType;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by MouMou on 25-10-16.
 */

public class SessionsListAdapter extends ArrayAdapter<Session> implements Observer {

    private List<Session> sessionList;
    private RecyclerView imageListView;
    private ImageRowAdapter imageRowAdapter;

    public SessionsListAdapter(Context context, List<Session> sessionList) {
        super(context, 0, sessionList);
        this.sessionList = sessionList;
    }

    @NonNull
    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {

        if (view == null) {
            view = LayoutInflater.from(getContext())
                    .inflate(R.layout.session_list_item, parent, false);
        }

        Session session = sessionList.get(position);

        ImageView imageView = (ImageView) view.findViewById(R.id.study_session_type_image);
        TextView date = (TextView) view.findViewById(R.id.study_session_date);
        TextView start = (TextView) view.findViewById(R.id.study_session_start);
        TextView end = (TextView) view.findViewById(R.id.study_session_end);
        imageListView = (RecyclerView) view.findViewById(R.id.image_wrapper);
        initImageListView(view, position);

        imageView.setImageBitmap(getBitmap(session.getType()));
        date.setText(session.getDate().toString("dd MMM"));
        start.setText(session.getStartTime().toString("HH:mm"));
        end.setText(session.getEndTime().toString("HH:mm"));
        return view;
    }

    private void initImageListView(View view, int position) {
        ArrayList<Bitmap> imageList = new ArrayList<>();
        sessionList.get(position).getSessionUsers().clear();
        sessionList.get(position)
                .getSessionUsers()
                .add(new User("Dennis",
                              "4322",
                              "https://graph.facebook.com/1094168823964890/picture?height=100"));

        for (int i = 0; i < sessionList.get(position).getSessionUsers().size(); i++) {
            User u = sessionList.get(position).getSessionUsers().get(i);
            if (u.getImage() == null) {
                Bitmap bmp = BitmapFactory.decodeResource(getContext().getResources(),
                                                          R.drawable.user32);
                imageList.add(bmp);
            } else {
                imageList.add(u.getImage());
            }
        }

        imageRowAdapter = new ImageRowAdapter(imageList);
        imageListView.setAdapter(imageRowAdapter);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(view.getContext(),
                                                                     LinearLayoutManager.HORIZONTAL,
                                                                     false);
        imageListView.setLayoutManager(manager);
    }

    private Bitmap getBitmap(SessionType type) {
        Bitmap bmp;
        switch (type) {
            case COFFEE:
                bmp = BitmapFactory.decodeResource(getContext().getResources(),
                                                   R.drawable.ic_coffee_black_48dp);
                break;
            case STUDY:
                bmp = BitmapFactory.decodeResource(getContext().getResources(),
                                                   R.drawable.ic_laptop_black_48dp);
                break;
            case LUNCH:
                bmp = BitmapFactory.decodeResource(getContext().getResources(),
                                                   R.drawable.ic_food_apple_black_48dp);
                break;
            default:
                bmp = BitmapFactory.decodeResource(getContext().getResources(),
                                                   R.drawable.ic_add_white_24dp);
        }
        return bmp;
    }

    @Override
    public void update(Observable observable, Object o) {
        notifyDataSetChanged();
        imageRowAdapter.notifyDataSetChanged();
    }
}
