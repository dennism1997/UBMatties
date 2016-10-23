package com.moumou.ubmatties.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import com.moumou.ubmatties.Adapters.AddMattiesGridAdapter;
import com.moumou.ubmatties.R;
import com.moumou.ubmatties.User;

import java.util.ArrayList;

/**
 * Created by MouMou on 09-10-16
 */

public class AddUserDialog extends Dialog {

    private GridView gridView;
    private AddMattiesGridAdapter gridAdapter;
    private ArrayList<User> userList;
    private Button okButton;
    private Button cancelButton;

    public AddUserDialog(Context context, final ArrayList<User> userList) {
        super(context);

        setContentView(R.layout.add_user_dialog);

        gridView = (GridView) findViewById(R.id.add_mattie_grid);
        gridAdapter = new AddMattiesGridAdapter(getContext(), userList);

        gridView.setAdapter(gridAdapter);
        gridAdapter.notifyDataSetChanged();

        okButton = (Button) findViewById(R.id.add_mattie_ok);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<User> added = new ArrayList<User>();
                for (int i = 0; i < userList.size(); i++) {
                    if (gridAdapter.getCheckedList().get(i)) {
                        added.add(userList.get(i));
                        System.out.println(userList.get(i).getName());
                    }
                }

                AddUserDialog.this.dismiss();
            }
        });
    }
}
