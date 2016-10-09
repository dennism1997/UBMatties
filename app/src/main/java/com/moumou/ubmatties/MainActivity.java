package com.moumou.ubmatties;

import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;

import com.moumou.ubmatties.Fragments.MattiesTabFragment;
import com.moumou.ubmatties.Fragments.SessionsTabFragment;
import com.moumou.ubmatties.Fragments.StudyTabFragment;

import java.security.MessageDigest;

import static com.moumou.ubmatties.globals.Globals.NUMBER_OF_TABS;

public class MainActivity extends AppCompatActivity {

    private static User self;
    private StudyTabFragment studyTabFragment;
    private SessionsTabFragment sessionTabFragment;
    private MattiesTabFragment mattiesTabFragment;

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

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

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

        logKeyHash();
    }

    //TODO remove when deploying
    private void logKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.moumou.ubmatties", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
}

