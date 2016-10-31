package com.moumou.ubmatties.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.moumou.ubmatties.Adapters.SessionsListAdapter;
import com.moumou.ubmatties.R;
import com.moumou.ubmatties.Session;
import com.moumou.ubmatties.globals.Globals;
import com.moumou.ubmatties.globals.SessionType;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by MouMou on 05-10-16
 */

public class SessionsTabFragment extends Fragment {

    private String JSON;
    private ListView sessionsListView;
    private SessionsListAdapter sessionsListAdapter;
    private ArrayList<Session> sessionsArrayList;
    private HttpURLConnection urlConnection;
    private URL dataBaseUrl;

    private SwipeRefreshLayout swipeContainer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sessions_tab, container, false);

        sessionsListView = (ListView) view.findViewById(R.id.sessions_list);
        sessionsArrayList = new ArrayList<>();
        sessionsListAdapter = new SessionsListAdapter(this.getContext(), sessionsArrayList);
        sessionsListView.setAdapter(sessionsListAdapter);

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.session_swipe_container);
        initSwipeToRefresh();


        getSessionsFromDB();

        return view;
    }

    private void initSwipeToRefresh() {
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getSessionsFromDB();
            }
        });
    }

    private void getSessionsFromDB() {
        class GetJSONData extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                String result = null;

                try {
                    dataBaseUrl = new URL(Globals.DB_SESSIONS_URL);
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
                parseJSONtoList();
                swipeContainer.setRefreshing(false);
            }
        }
        GetJSONData g = new GetJSONData();
        g.execute();
    }

    private void parseJSONtoList() {
        sessionsArrayList.clear();

        try {
            JSONObject jsonObject = new JSONObject(JSON);
            JSONArray array = jsonObject.getJSONArray(Globals.TAG_SESSIONS);
            System.out.println(jsonObject.toString());
            for (int i = 0; i < array.length(); i++) {
                JSONObject o = array.getJSONObject(i);

                int id = Integer.valueOf(o.getString(Globals.TAG_ID));
                SessionType type = SessionType.fromInt(Integer.valueOf(o.getString(Globals.TAG_TYPE)));
                LocalDate date = LocalDate.parse(o.getString(Globals.TAG_DATE));
                LocalTime start = LocalTime.parse(o.getString(Globals.TAG_START));
                LocalTime end = LocalTime.parse(o.getString(Globals.TAG_END));
                Session s = new Session(id, type, date, start, end);
                sessionsArrayList.add(s);
            }

            sessionsListAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
