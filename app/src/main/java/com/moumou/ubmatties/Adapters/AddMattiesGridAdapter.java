package com.moumou.ubmatties.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.moumou.ubmatties.R;
import com.moumou.ubmatties.User;

import java.util.List;

/**
 * Created by MouMou on 09-10-16
 */

public class AddMattiesGridAdapter extends ArrayAdapter<User> {

    private List<User> userList;

    public AddMattiesGridAdapter(Context context, List<User> userList) {
        super(context, 0, userList);
        this.userList = userList;
    }

    @NonNull
    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {

        if (view == null) {
            view = LayoutInflater.from(getContext())
                    .inflate(R.layout.add_user_grid_item, parent, false);
        }
        User user = userList.get(position);

        final ImageButton imageButton = (ImageButton) view.findViewById(R.id.add_mattie_image_button);
        TextView name = (TextView) view.findViewById(R.id.add_mattie_name);

        name.setText(user.getName());
        if (user.getImage() == null) {
            Bitmap bmp = BitmapFactory.decodeResource(getContext().getResources(),
                                                      R.drawable.user32);
            imageButton.setImageBitmap(bmp);
        } else {
            imageButton.setImageBitmap(user.getImage());
        }

        final CheckBox checkBox = (CheckBox) view.findViewById(R.id.add_mattie_checkbox);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkBox.isChecked()) {
                    Bitmap bmp = BitmapFactory.decodeResource(getContext().getResources(),
                                                              R.drawable.ic_laptop_black_48dp);
                    imageButton.setImageBitmap(bmp);
                    checkBox.setChecked(true);
                } else {
                    Bitmap bmp = BitmapFactory.decodeResource(getContext().getResources(),
                                                              R.drawable.user32);
                    imageButton.setImageBitmap(bmp);
                    checkBox.setChecked(false);
                }
            }
        });

        return view;
    }
}
