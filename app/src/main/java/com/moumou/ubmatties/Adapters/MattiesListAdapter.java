package com.moumou.ubmatties.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.moumou.ubmatties.R;
import com.moumou.ubmatties.User;

import java.util.ArrayList;

/**
 * Created by MouMou on 04-10-16
 */

public class MattiesListAdapter extends ArrayAdapter<User> {

    private ArrayList<User> users;

    public MattiesListAdapter(Context context, ArrayList<User> users) {
        super(context, 0, users);
        this.users = users;
    }

    @NonNull
    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.matties_list_item, parent, false);
        }

        User user = users.get(position);
        TextView name = (TextView) view.findViewById(R.id.matties_name);
        ImageView imageView = (ImageView) view.findViewById(R.id.matties_image);
        ImageButton button = (ImageButton) view.findViewById(R.id.matties_button);

        name.setText(user.getName());
        if (user.getImage() == null) {
            Bitmap bmp = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.user32);
            imageView.setImageBitmap(bmp);
        } else {
            imageView.setImageBitmap(user.getImage());
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO code to add mattie to a session
            }
        });


        return view;
    }

}
