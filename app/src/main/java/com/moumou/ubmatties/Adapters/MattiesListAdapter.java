package com.moumou.ubmatties.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.moumou.ubmatties.R;
import com.moumou.ubmatties.User;

import java.util.ArrayList;

/**
 * Created by MouMou on 04-10-16.
 */

public class MattiesListAdapter extends ArrayAdapter<User> {

    private ArrayList<User> users;
    //private ImageView image;
    private TextView name;
    private Button button;

    public MattiesListAdapter(Context context, ArrayList<User> users) {
        super(context, 0, users);
        this.users = users;
    }

    @NonNull
    @Override
    public View getView(int position, View view, ViewGroup parent) {

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.matties_list_item, parent, false);
            ;
        }
        name = (TextView) view.findViewById(R.id.matties_name);
        //image = (ImageView) view.findViewById(R.id.matties_image);
        button = (Button) view.findViewById(R.id.matties_button);

        name.setText(users.get(position).getName());
        //.setImageResource(R.drawable.com_facebook_send_button_icon);
        button.setText(R.string.add_mattie_button);

        return view;

    }
}
