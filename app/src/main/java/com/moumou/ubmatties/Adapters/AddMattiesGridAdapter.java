package com.moumou.ubmatties.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MouMou on 09-10-16
 */

public class AddMattiesGridAdapter extends ArrayAdapter<User> {

    private ArrayList<User> userList;
    ColorMatrix matrix = new ColorMatrix();
    ColorMatrixColorFilter filter;
    private List<Boolean> checkedList;

    public AddMattiesGridAdapter(Context context, ArrayList<User> userList) {
        super(context, 0, userList);
        this.userList = userList;
        checkedList = new ArrayList<>(userList.size());
        for (User u : userList) {
            checkedList.add(false);
        }
        matrix.setSaturation(0);
        filter = new ColorMatrixColorFilter(matrix);
    }

    @NonNull
    @Override
    public View getView(final int position, View view, @NonNull ViewGroup parent) {

        if (view == null) {
            view = LayoutInflater.from(getContext())
                    .inflate(R.layout.add_user_grid_item, parent, false);
        }
        final User user = userList.get(position);

        final ImageButton imageButton = (ImageButton) view.findViewById(R.id.add_mattie_image_button);
        TextView name = (TextView) view.findViewById(R.id.add_mattie_name);

        name.setText(user.getName());
        if (user.getImage() == null) {
            Bitmap bmp = BitmapFactory.decodeResource(getContext().getResources(),
                                                      R.drawable.user32);
            imageButton.setImageBitmap(bmp);
            //imageButton.setColorFilter(Color.argb(150, 255, 255, 255));
            if (!checkedList.get(position)) {
                imageButton.setColorFilter(filter);
            }
        } else {
            imageButton.setImageBitmap(user.getImage());
            //imageButton.setColorFilter(Color.argb(150, 255, 255, 255));
            if (!checkedList.get(position)) {
                imageButton.setColorFilter(filter);
            }
        }

        final CheckBox checkBox = (CheckBox) view.findViewById(R.id.add_mattie_checkbox);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    imageButton.setColorFilter(filter);
                    checkBox.setChecked(false);
                    checkedList.set(position, false);
                } else {
                    imageButton.clearColorFilter();
                    checkBox.setChecked(true);
                    checkedList.set(position, true);
                }
            }
        });
        return view;
    }

    public List<Boolean> getCheckedList() {
        return checkedList;
    }
}
