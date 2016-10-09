package com.moumou.ubmatties.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.widget.GridView;

import com.moumou.ubmatties.Adapters.AddMattiesGridAdapter;
import com.moumou.ubmatties.R;
import com.moumou.ubmatties.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MouMou on 09-10-16.
 */

public class AddUserDialog extends Dialog {

    private GridView gridView;
    private AddMattiesGridAdapter gridAdapter;
    private ArrayList<User> userList;

    public AddUserDialog(Context context, List<User> userList) {
        super(context);

        setContentView(R.layout.add_user_dialog);

        gridView = (GridView) findViewById(R.id.add_mattie_grid);
        gridAdapter = new AddMattiesGridAdapter(getContext(), userList);

        gridView.setAdapter(gridAdapter);
        gridAdapter.notifyDataSetChanged();
    }
}
