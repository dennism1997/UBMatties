package com.moumou.ubmatties.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.moumou.ubmatties.Adapters.SessionsListAdapter;
import com.moumou.ubmatties.Loaders.SessionsLoader;
import com.moumou.ubmatties.R;
import com.moumou.ubmatties.Session;
import com.moumou.ubmatties.globals.Globals;

import java.util.ArrayList;

/**
 * Created by MouMou on 05-10-16
 */

public class SessionsTabFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList> {

    private SessionsListAdapter sessionsListAdapter;
    private ArrayList<Session> sessionsArrayList;

    private SwipeRefreshLayout swipeContainer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sessions_tab, container, false);

        ListView sessionsListView = (ListView) view.findViewById(R.id.sessions_list);
        sessionsArrayList = new ArrayList<>();
        sessionsListAdapter = new SessionsListAdapter(this.getContext(), sessionsArrayList);
        sessionsListView.setAdapter(sessionsListAdapter);

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.session_swipe_container);
        initSwipeToRefresh();

        getLoaderManager().initLoader(Globals.LOADER_SESSIONS, null, this).forceLoad();

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
        getLoaderManager().getLoader(Globals.LOADER_SESSIONS).forceLoad();
    }

    @Override
    public void onLoaderReset(Loader<ArrayList> loader) {
    }

    @Override
    public Loader<ArrayList> onCreateLoader(int id, Bundle args) {
        return new SessionsLoader(getContext());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList> loader, ArrayList data) {
        if (data == null) {
            return;
        }
        sessionsArrayList.clear();
        for (int i = 0; i < data.size(); i++) {
            sessionsArrayList.add((Session) data.get(i));
        }
        swipeContainer.setRefreshing(false);
        sessionsListAdapter.notifyDataSetChanged();
    }
}
