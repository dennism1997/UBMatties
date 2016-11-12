package com.moumou.ubmatties.Loaders;

import android.content.Context;

import com.moumou.ubmatties.Session;
import com.moumou.ubmatties.globals.Globals;
import com.moumou.ubmatties.globals.SessionType;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by MouMou on 12-11-16
 */

public class SessionsLoader extends DBLoader {

    private HttpURLConnection urlConnection;
    private String result;

    public SessionsLoader(Context context) {
        super(context);
    }

    @Override
    public ArrayList<Session> loadInBackground() {
        try {
            URL dataBaseUrl = new URL(Globals.DB_SESSIONS_URL);
            urlConnection = (HttpURLConnection) dataBaseUrl.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            result = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }

        return parseJSONtoList(result);
    }

    @Override
    public void deliverResult(ArrayList data) {
        super.deliverResult(data);
    }

    private ArrayList<Session> parseJSONtoList(String JSON) {

        ArrayList<Session> sessions = new ArrayList<>();
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
                sessions.add(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sessions;
    }
}
