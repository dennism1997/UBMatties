package com.moumou.ubmatties.Loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.moumou.ubmatties.Session;
import com.moumou.ubmatties.globals.Globals;
import com.moumou.ubmatties.globals.SessionType;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by MouMou on 12-11-16
 */

public class AddSessionLoader extends AsyncTaskLoader<Boolean> {

    private Session s;
    private HttpURLConnection urlConnection;

    public AddSessionLoader(Context context, Session session) {
        super(context);
        this.s = session;
    }

    @Override
    public Boolean loadInBackground() {
        String result = "";

        try {
            URL dataBaseUrl = new URL(Globals.DB_INSERT_SESSION + s.getHost().getId() + "&type=" +
                                      SessionType.toInt(s.getType()) + "&date=" +
                                      s.getDate().toString("YYYY-MM-dd") + "&start=" +
                                      s.getStartTime().toString("HH:mm:ss") + "&end=" +
                                      s.getEndTime().toString("HH:mm:ss"));
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
            System.err.println(result);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return result.equals("success\n");
    }

    @Override
    public void deliverResult(Boolean data) {
        super.deliverResult(data);
    }
}
