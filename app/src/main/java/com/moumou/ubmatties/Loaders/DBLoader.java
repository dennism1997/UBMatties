package com.moumou.ubmatties.Loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.ArrayList;

/**
 * Created by MouMou on 11-11-16
 */

public abstract class DBLoader extends AsyncTaskLoader<ArrayList> {

    public DBLoader(Context context) {
        super(context);
    }

    @Override
    public ArrayList loadInBackground() {
        return null;
    }
}
