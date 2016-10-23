package com.moumou.ubmatties.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.moumou.ubmatties.R;
import com.moumou.ubmatties.globals.Globals;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by MouMou on 05-10-16
 */

public class SessionsTabFragment extends Fragment {

    private JSONArray sessions = null;
    private String JSON;
    private ListView sessionsList;
    private ArrayList<HashMap<String, String>> sessionsArrayList;
    private HttpURLConnection urlConnection;
    private URL dataBaseUrl;
    private TextView textView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sessions_tab, container, false);

        sessionsList = (ListView) view.findViewById(R.id.sessions_list);
        textView = (TextView) view.findViewById(R.id.sessions_textView);
        textView.setText("hoi");
        getSessionsFromDB();
        return view;
    }

    private void getSessionsFromDB() {
        class GetJSONData extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                String result = null;

                try {
                    dataBaseUrl = new URL(Globals.DB_HOST);
                    urlConnection = (HttpURLConnection) dataBaseUrl.openConnection();
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    urlConnection.disconnect();
                }
                return result;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                JSON = s;
                textView.setText(JSON);
            }
        }
        GetJSONData g = new GetJSONData();
        g.execute();
    }
}
