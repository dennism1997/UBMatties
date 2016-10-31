package com.moumou.ubmatties.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.moumou.ubmatties.R;
import com.moumou.ubmatties.Session;
import com.moumou.ubmatties.globals.SessionType;

import java.util.List;

/**
 * Created by MouMou on 25-10-16.
 */

public class SessionsListAdapter extends ArrayAdapter<Session> {

    private List<Session> sessionList;

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

        imageView.setImageBitmap(getBitmap(session.getType()));
        date.setText(session.getDate().toString("dd MMM"));
        start.setText(session.getStartTime().toString("HH:mm"));
        end.setText(session.getEndTime().toString("HH:mm"));
        return view;
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
}
