package com.moumou.ubmatties.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.widget.AppInviteDialog;
import com.moumou.ubmatties.Adapters.MattiesListAdapter;
import com.moumou.ubmatties.MainActivity;
import com.moumou.ubmatties.R;
import com.moumou.ubmatties.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.moumou.ubmatties.globals.Globals.INVITE_IMAGE_URL;
import static com.moumou.ubmatties.globals.Globals.INVITE_URL;

/**
 * Created by MouMou on 04-10-16
 */

public class MattiesTabFragment extends Fragment {

    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private ArrayList<String> permissionList;
    private AlertDialog.Builder builder;
    private ArrayList<User> matties;
    private TextView textView;
    private MattiesListAdapter mattiesListAdapter;
    private SwipeRefreshLayout swipeContainer;
    private Profile profile;
    private User self;


    public MattiesTabFragment() {
        super();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.matties_tab, container, false);

        this.setRetainInstance(true);

        builder = new AlertDialog.Builder(MattiesTabFragment.this.getContext());
        textView = (TextView) view.findViewById(R.id.textView);

        loginButton = (LoginButton) view.findViewById(R.id.login_button);

        loginButton.setReadPermissions(permissionList);

        loginButton.setFragment(this);
        if (profile != null) {
            loginButton.setVisibility(View.GONE);
            String text = getString(R.string.logged_in_as) + profile.getName();
            textView.setText(text);
        } else {
            textView.setText(R.string.not_logged_in);
        }
        handleLoginButton();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initListView(view);
        getProfilePictures();

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.matties_swipe_container);
        initSwipeToRefresh();

        if (MainActivity.getSelf() != null) {
            self = MainActivity.getSelf();
        }

//        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab_matties);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);

        FacebookSdk.sdkInitialize(getContext());
        callbackManager = CallbackManager.Factory.create();


        permissionList = new ArrayList<>();
        permissionList.add("user_friends");

        new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(
                    Profile oldProfile,
                    Profile currentProfile) {
                if (currentProfile != null) {
                    profile = currentProfile;
                    String text = getString(R.string.logged_in_as) + currentProfile.getName();
                    textView.setText(text);
                    loginButton.setVisibility(View.GONE);
                    getFriendsList();
                    getProfilePictures();
                    self = new User(profile.getName(), profile.getId(), profile.getLinkUri().toString());
                    MainActivity.setSelf(self);
                } else {
                    profile = null;
                    loginButton.setVisibility(View.VISIBLE);
                    swipeContainer.setRefreshing(false);
                    //facebookOption.setTitle(R.string.com_facebook_loginview_log_out_action);
                }
            }
        };


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_matties, menu);


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.option_facebook) {
            if (AccessToken.getCurrentAccessToken() != null) {
                LoginManager.getInstance().logOut();
                matties.clear();
                mattiesListAdapter.notifyDataSetChanged();
                textView.setText(getString(R.string.not_logged_in));
            } else {
                handleLoginOption();
            }

        } else if (id == R.id.option_invite) {
            openInviteDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void initListView(View view) {
        if (matties == null) matties = new ArrayList<>();

        mattiesListAdapter = new MattiesListAdapter(getContext(), matties);

        ListView fbFriendsListView = (ListView) view.findViewById(R.id.fb_friends);
        fbFriendsListView.setAdapter(mattiesListAdapter);

    }

    private void initSwipeToRefresh() {
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFriendsList();
                getProfilePictures();
            }
        });
    }

    private void fillFriendsList(JSONArray jsonArray) throws JSONException {
        if (matties == null) {
            matties = new ArrayList<>();
        }

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            User user = new User(object.get("name").toString(), object.get("id").toString());
            if (!matties.contains(user)) {
                matties.add(user);
            }
        }

        mattiesListAdapter.notifyDataSetChanged();
    }

    private void getFriendsList() {
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            System.out.println(response.getJSONObject().getJSONObject("friends").getJSONArray("data"));
                            //response.getJSONObject().getJSONObject("friends").getJSONArray("data").getJSONObject(0).get("name").toString()
                            fillFriendsList(response.getJSONObject().getJSONObject("friends").getJSONArray("data"));
                            getProfilePictures();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                            matties.clear();
                            mattiesListAdapter.notifyDataSetChanged();
                            textView.setText(getString(R.string.not_logged_in));
                            swipeContainer.setRefreshing(false);
                            showDialog("Can't connect to Facebook.. \nAre you connected to the Internet?");
                        }


                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "friends");
        request.setParameters(parameters);
        request.executeAsync();

    }

    public void getProfilePictures() {


        for (final User user : matties) {
            GraphRequest request = new GraphRequest(AccessToken.getCurrentAccessToken(),
                    "/" + user.getId() + "/picture",
                    null,
                    HttpMethod.GET,
                    new GraphRequest.Callback() {
                        @Override
                        public void onCompleted(GraphResponse response) {
                            System.out.println("profile picture response:");
                            System.out.println(response.getRawResponse());
                            try {
                                String url = response.getJSONObject().getJSONObject("data").getString("url");
                                user.setImage(url);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            mattiesListAdapter.notifyDataSetChanged();
                            swipeContainer.setRefreshing(false);
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("redirect", "false");
            parameters.putString("type", "large");
            request.setParameters(parameters);
            request.executeAsync();
        }
    }

    private void openInviteDialog() {

        if (AppInviteDialog.canShow()) {
            AppInviteContent content = new AppInviteContent.Builder().setApplinkUrl(INVITE_URL).
                    setPreviewImageUrl(INVITE_IMAGE_URL).build();

            AppInviteDialog appInviteDialog = new AppInviteDialog(getActivity());
            appInviteDialog.registerCallback(callbackManager, new FacebookCallback<AppInviteDialog.Result>() {
                @Override
                public void onSuccess(AppInviteDialog.Result result) {
                }

                @Override
                public void onCancel() {

                }

                @Override
                public void onError(FacebookException error) {

                }
            });
            appInviteDialog.show(content);
        }
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
                //facebookOption.setTitle(R.string.com_facebook_loginview_log_out_action);
                //loginButton.setVisibility(View.GONE);
                getFriendsList();
                getProfilePictures();
            }

            @Override
            public void onCancel() {
                showDialog("Can't login to facebook");
                matties.clear();
                mattiesListAdapter.notifyDataSetChanged();
                textView.setText(getString(R.string.not_logged_in));
            }

            @Override
            public void onError(FacebookException exception) {
                showDialog("Can't login to facebook");
                exception.printStackTrace();
            }
        });

    }


}
