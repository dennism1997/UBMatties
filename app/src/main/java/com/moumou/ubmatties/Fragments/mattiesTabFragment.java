package com.moumou.ubmatties.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moumou.ubmatties.R;

/**
 * Created by dennis on 04-10-16.
 */

public class MattiesTabFragment extends Fragment {

    public MattiesTabFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.matties_tab, container, false);
        TextView textView = (TextView) view.findViewById(R.id.textView);
        textView.setText("second tab");
        return view;
    }
}
