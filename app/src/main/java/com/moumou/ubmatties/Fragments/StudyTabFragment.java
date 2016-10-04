package com.moumou.ubmatties.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moumou.ubmatties.R;

/**
 * Created by MouMou on 04-10-2016
 */

public class StudyTabFragment extends Fragment {

    public StudyTabFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.study_tab, container, false);
        TextView textView = (TextView) view.findViewById(R.id.textView);
        textView.setText("first tab");
        return view;
    }
}
