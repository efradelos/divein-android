package com.example.efradelos.divein;

import android.app.Application;

import com.example.efradelos.divein.data.DatabaseManager;

/**
 * Created by efradelos on 9/10/15.
 */
public class DiveInApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DatabaseManager.initDb(this);
    }
}
