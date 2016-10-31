package com.moumou.ubmatties;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.moumou.ubmatties.Fragments.MattiesTabFragment;
import com.moumou.ubmatties.Fragments.SessionsTabFragment;
import com.moumou.ubmatties.Fragments.StudyTabFragment;
import com.moumou.ubmatties.globals.Globals;

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

import static com.moumou.ubmatties.globals.Globals.NUMBER_OF_TABS;

public class MainActivity extends AppCompatActivity {

    private static User self;
    private StudyTabFragment studyTabFragment;
    private SessionsTabFragment sessionTabFragment;
    private MattiesTabFragment mattiesTabFragment;

    private CallbackManager callbackManager;
    private ArrayList<String> permissionList;
    private Profile profile;

    private HttpURLConnection urlConnection;
    private URL dataBaseUrl;
    private String JSON;

    //private FloatingActionButton fabStudy;
    //private FloatingActionButton fabMatties;
    private static AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(
                getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        if (studyTabFragment == null) studyTabFragment = new StudyTabFragment();
        studyTabFragment.setRetainInstance(true);
        if (sessionTabFragment == null) sessionTabFragment = new SessionsTabFragment();
        sessionTabFragment.setRetainInstance(true);
        if (mattiesTabFragment == null) mattiesTabFragment = new MattiesTabFragment();
        mattiesTabFragment.setRetainInstance(true);

        builder = new AlertDialog.Builder(this);

        FacebookSdk.sdkInitialize(this);
        callbackManager = CallbackManager.Factory.create();

        permissionList = new ArrayList<>();
        permissionList.add("user_friends");

        new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                if (currentProfile != null) {
                    profile = currentProfile;
                    MainActivity.setSelf(new User(profile.getName(),
                                                  profile.getId(),
                                                  profile.getProfilePictureUri(100, 100)
                                                          .toString()));
                    logintoDB();
                    Snackbar.make(findViewById(R.id.main_content),
                                  "main" + getString(R.string.logged_in_as) + getSelf().getName(),
                                  Snackbar.LENGTH_LONG).show();
                } else {
                    profile = null;
                    Snackbar.make(findViewById(R.id.main_content),
                                  "Log in at the My Matties Tab!",
                                  Snackbar.LENGTH_LONG).show();
                }
            }
        };
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return studyTabFragment;
                case 1:
                    return sessionTabFragment;
                case 2:
                    return new MattiesTabFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return NUMBER_OF_TABS;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.tab1);
                case 1:
                    return getString(R.string.tab2);
                case 2:
                    return getString(R.string.tab3);
            }
            return null;
        }
    }

    public static User getSelf() {
        return self;
    }

    public static void setSelf(User self) {
        MainActivity.self = self;
    }

    public static void showAlertDialog(String message) {

        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setTitle("Error");
        dialog.setMessage(message);

        dialog.show();
    }

    public void logintoDB() {
        class GetJSONData extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                String result = null;

                try {
                    dataBaseUrl = new URL(Globals.DB_USER + self.getId());
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
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
                return result;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                JSON = s;
                parseUser();
            }
        }
        GetJSONData g = new GetJSONData();
        g.execute();
    }

    public void parseUser() {
        try {
            JSONObject object = new JSONObject(JSON);
            JSONArray array = object.getJSONArray(Globals.TAG_USER);
            System.out.println(object.toString());
            if (!(array.length() > 0)) {
                insertUser(self.getId(), self.getName(), self.getImageURL());
            } else {
                JSONObject user = array.getJSONObject(0);
                System.out.println(user.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void insertUser(final String id, final String name, final String image) {
        class GetJSONData extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                String result = null;

                try {
                    URL dataBaseUrl = new URL(
                            Globals.DB_INSERT_USER + id + "&name=" + name + "&image=" + image);
                    HttpURLConnection urlConnection = (HttpURLConnection) dataBaseUrl.openConnection();
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
                }
                return result;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (!s.equals("success\n")) {
                    showAlertDialog("cant insert user into database\n" + s);
                }
            }
        }
        GetJSONData g = new GetJSONData();
        g.execute();
    }
}

