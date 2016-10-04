package com.moumou.ubmatties.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.moumou.ubmatties.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Created by MouMou on 04-10-16
 */

public class MattiesTabFragment extends Fragment {

    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private LoginResult loginResult;
    private TextView textView;
    private ProfileTracker profileTracker;
    private Collection<String> permissionList;
    private LinkedList<String> friendsList;

    public MattiesTabFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.matties_tab, container, false);
        textView = (TextView) view.findViewById(R.id.textView);
        textView.setText("second tab");

        loginButton = (LoginButton) view.findViewById(R.id.login_button);

        //loginButton.setReadPermissions(permissionList);

        // If using in a fragment
        loginButton.setFragment(this);
        // Other app specific specialization
        LoginManager.getInstance().logInWithReadPermissions(MattiesTabFragment.this, permissionList);

        // Callback registration
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

                //loginButton.setVisibility(View.GONE);
            }

            @Override
            public void onCancel() {
                textView.setText("Login attempt canceled.");
            }

            @Override
            public void onError(FacebookException exception) {
                textView.setText("Login attempt failed.");
                System.out.println(exception.toString());
            }
        });

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getContext());
        callbackManager = CallbackManager.Factory.create();


        permissionList = new LinkedList<String>();
        permissionList.add("email");
        permissionList.add("user_friends");

        friendsList = new LinkedList();

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(
                    Profile oldProfile,
                    Profile currentProfile) {
                if (currentProfile != null) {
                    textView.setText(currentProfile.getName());
                    //loginButton.setVisibility(View.GONE);
                    readFriendsList(currentProfile);
                } else {
                    loginButton.setVisibility(View.VISIBLE);
                }
            }
        };


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void fillFriendsList(JSONArray jsonArray) throws JSONException {
        if (friendsList == null) {
            friendsList = new LinkedList();
        }
        System.out.println("size = " + jsonArray.length());

        for (int i = 0; i < jsonArray.length(); i++) {
            friendsList.add(jsonArray.get(i).toString());
            System.out.println(jsonArray.get(i).toString());
        }

        //textView.setText(friendsList.get(0));
    }

    public void readFriendsList(final Profile currentProfile) {
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
                            System.out.println(response.getJSONObject().toString());
                            JSONArray jsonArrayFriends = response.getJSONObject().getJSONArray("data");
                            fillFriendsList(jsonArrayFriends);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();
    }
}
