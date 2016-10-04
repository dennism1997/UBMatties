package com.moumou.ubmatties.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.moumou.ubmatties.Adapters.MattiesListAdapter;
import com.moumou.ubmatties.R;
import com.moumou.ubmatties.User;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Created by MouMou on 04-10-16
 */

public class MattiesTabFragment extends Fragment {

    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private LoginResult loginResult;
    private ProfileTracker profileTracker;
    private Collection<String> permissionList;
    private LinkedList<String> friendsList;
    private AlertDialog.Builder builder;
    private MenuItem facebookOption;
    private ListView FbFriendsListView;
    private ArrayList<User> matties;
    private TextView textView;
    private MattiesListAdapter mattiesListAdapter;
    public MattiesTabFragment() {
        super();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.matties_tab, container, false);

        builder = new AlertDialog.Builder(MattiesTabFragment.this.getContext());
        textView = (TextView) view.findViewById(R.id.textView);

        loginButton = (LoginButton) view.findViewById(R.id.login_button);

        //loginButton.setReadPermissions(permissionList);

        loginButton.setFragment(this);
        loginButton.setReadPermissions("user_friends");
        //LoginManager.getInstance().logInWithReadPermissions(MattiesTabFragment.this, permissionList);
        handleLoginButton();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initListView(view);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        FacebookSdk.sdkInitialize(getContext());
        callbackManager = CallbackManager.Factory.create();


        permissionList = new LinkedList<>();
        permissionList.add("email");
        permissionList.add("user_friends");

        friendsList = new LinkedList<>();

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(
                    Profile oldProfile,
                    Profile currentProfile) {
                if (currentProfile != null) {
                    textView.setText(currentProfile.getName());
                    loginButton.setVisibility(View.GONE);
                    readFriendsList(currentProfile);
                } else {
                    loginButton.setVisibility(View.VISIBLE);
                    facebookOption.setTitle(R.string.com_facebook_loginview_log_out_action);
                }
            }
        };


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_matties, menu);
        facebookOption = menu.findItem(R.id.option_facebook);


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.option_facebook) {
            if (facebookOption.getTitle().equals(getString(R.string.com_facebook_loginview_log_out_action))) {
                LoginManager.getInstance().logOut();
            } else {
                handleLoginOption();
            }

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void initListView(View view) {
        User u1 = new User("Stephan Kapteijn");


        matties = new ArrayList<>();
        matties.add(u1);
        matties.add(new User("Tom Kempenaar"));
        matties.add(new User("Tom Kempenaar"));
        matties.add(new User("Tom Kempenaar"));
        matties.add(new User("Tom Kempenaar"));
        matties.add(new User("Tom Kempenaar"));
        matties.add(new User("Tom Kempenaar"));
        matties.add(new User("Tom Kempenaar"));
        matties.add(new User("Tom Kempenaar"));
        matties.add(new User("Tom Kempenaar"));
        matties.add(new User("Tom Kempenaar"));
        mattiesListAdapter = new MattiesListAdapter(getContext(), matties);

        FbFriendsListView = (ListView) view.findViewById(R.id.fb_friends);
        FbFriendsListView.setAdapter(mattiesListAdapter);

    }

    private void fillFriendsList(JSONArray jsonArray) throws JSONException {
        if (friendsList == null) {
            friendsList = new LinkedList<>();
        }
        System.out.println("size = " + jsonArray.length());

        for (int i = 0; i < jsonArray.length(); i++) {
            friendsList.add(jsonArray.get(i).toString());
            System.out.println(jsonArray.get(i).toString());
        }
    }

    private void readFriendsList(final Profile currentProfile) {
        /* make the API call */
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + currentProfile.getId() + "",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                            /* handle the result */
                        try {
                            JSONArray jsonArrayFriends = response.getJSONObject().getJSONArray("data");
                            fillFriendsList(jsonArrayFriends);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (NullPointerException e) {
                            showDialog("Can't connect to Facebook");
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();
    }

    private void showDialog(String message) {
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

    private void handleLoginOption() {
        LoginManager.getInstance().logInWithReadPermissions(MattiesTabFragment.this, permissionList);
    }

    private void handleLoginButton() {
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                textView.setText(

                        "User ID: "
                                + loginResult.getAccessToken().getUserId()
                                + "\n" +
                                "Auth Token: "
                                + loginResult.getAccessToken().getToken()
                );
                facebookOption.setTitle(R.string.com_facebook_loginview_log_out_action);
                //loginButton.setVisibility(View.GONE);
            }

            @Override
            public void onCancel() {
                textView.setText("Login attempt canceled.");
                showDialog("Can't login to facebook");
            }

            @Override
            public void onError(FacebookException exception) {
                textView.setText("Login attempt failed.");
                showDialog("Can't login to facebook");
                exception.printStackTrace();
            }
        });

    }
}
