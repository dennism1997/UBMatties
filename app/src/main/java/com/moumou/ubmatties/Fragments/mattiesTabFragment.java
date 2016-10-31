package com.moumou.ubmatties.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import java.util.Observable;
import java.util.Observer;

import static com.moumou.ubmatties.globals.Globals.INVITE_IMAGE_URL;
import static com.moumou.ubmatties.globals.Globals.INVITE_URL;

/**
 * Created by MouMou on 04-10-16
 */

public class MattiesTabFragment extends Fragment implements Observer {

    private CallbackManager callbackManager;
    private ArrayList<String> permissionList;
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
        textView = (TextView) view.findViewById(R.id.textView);
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.matties_swipe_container);
        initSwipeToRefresh();
        initListView(view);
        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (MainActivity.getSelf() != null) {
            self = MainActivity.getSelf();
        }

        new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                if (currentProfile != null) {
                    profile = currentProfile;
                    String text = getString(R.string.logged_in_as) + currentProfile.getName();
                    textView.setText(text);
                    getFriendsList();

                    self = new User(profile.getName(),
                                    profile.getId(),
                                    profile.getLinkUri().toString());
                    MainActivity.setSelf(self);
                } else {
                    profile = null;
                }
                swipeContainer.setRefreshing(false);
            }
        };

        getFriendsList();

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
                                                                     System.out.println(response.getJSONObject()
                                                                                                .getJSONObject(
                                                                                                        "friends")
                                                                                                .getJSONArray(
                                                                                                        "data"));
                                                                     //response.getJSONObject().getJSONObject("friends").getJSONArray("data").getJSONObject(0).get("name").toString()
                                                                     fillFriendsList(response.getJSONObject()
                                                                                             .getJSONObject(
                                                                                                     "friends")
                                                                                             .getJSONArray(
                                                                                                     "data"));
                                                                     getProfilePictures();
                                                                 } catch (JSONException e) {
                                                                     e.printStackTrace();
                                                                 } catch (NullPointerException e) {
                                                                     e.printStackTrace();
                                                                     matties.clear();
                                                                     mattiesListAdapter.notifyDataSetChanged();
                                                                     textView.setText(getString(R.string.not_logged_in));
                                                                     swipeContainer.setRefreshing(
                                                                             false);
                                                                     MainActivity.showAlertDialog(
                                                                             getString(R.string.cant_connect_fb));
                                                                 } finally {
                                                                     mattiesListAdapter.notifyDataSetChanged();
                                                                     swipeContainer.setRefreshing(
                                                                             false);
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
                                                            System.out.println(response.getRawResponse());
                                                            try {
                                                                String url = response.getJSONObject()
                                                                        .getJSONObject("data")
                                                                        .getString("url");
                                                                user.setImage(url);
                                                                user.addObserver(MattiesTabFragment.this);
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }
                                                            mattiesListAdapter.notifyDataSetChanged();
                                                        }
                                                    });
            Bundle parameters = new Bundle();
            parameters.putString("redirect", "false");
            parameters.putString("type", "large");
            request.setParameters(parameters);
            request.executeAsync();
        }
        mattiesListAdapter.notifyDataSetChanged();
        swipeContainer.setRefreshing(false);
    }

    @Override
    public void update(Observable observable, Object o) {
        mattiesListAdapter.notifyDataSetChanged();
    }

    private void openInviteDialog() {

        if (AppInviteDialog.canShow()) {
            AppInviteContent content = new AppInviteContent.Builder().setApplinkUrl(INVITE_URL).
                    setPreviewImageUrl(INVITE_IMAGE_URL).build();

            AppInviteDialog appInviteDialog = new AppInviteDialog(getActivity());
            appInviteDialog.registerCallback(callbackManager,
                                             new FacebookCallback<AppInviteDialog.Result>() {
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

    private void handleLoginOption() {
        LoginManager.getInstance()
                .logInWithReadPermissions(MattiesTabFragment.this, permissionList);
    }
}
