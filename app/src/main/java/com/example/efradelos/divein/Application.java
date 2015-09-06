package com.example.efradelos.divein;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Manager;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryEnumerator;
import com.couchbase.lite.QueryRow;
import com.couchbase.lite.android.AndroidContext;
import com.couchbase.lite.replicator.Replication;
import com.couchbase.lite.util.Log;
import com.example.efradelos.divein.documents.Diver;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;

public class Application extends android.app.Application {
    public static final String LOG_TAG = "EFX";

    private Manager mManager;
    private Database mDatabase;

    public Database getDatabaseInstance() {
        return mDatabase;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initDb();
        startReplications();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        mManager.close();
    }


    private void initDb() {
        try {
            Manager.enableLogging(LOG_TAG, Log.VERBOSE);
            Manager.enableLogging(Log.TAG, Log.VERBOSE);
            Manager.enableLogging(Log.TAG_SYNC_ASYNC_TASK, Log.VERBOSE);
            Manager.enableLogging(Log.TAG_SYNC, Log.VERBOSE);
            Manager.enableLogging(Log.TAG_QUERY, Log.VERBOSE);
            Manager.enableLogging(Log.TAG_VIEW, Log.VERBOSE);
            Manager.enableLogging(Log.TAG_DATABASE, Log.VERBOSE);

            mManager = new Manager(new AndroidContext(getApplicationContext()), Manager.DEFAULT_OPTIONS);
            mDatabase = mManager.getDatabase(Constants.DB_NAME);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error getting database", e);
        }
    }


    private void startReplications() {
        try {
            Replication pull = this.getDatabaseInstance().createPullReplication(new URL(Constants.DB_SYNC_URL));
            Replication push = this.getDatabaseInstance().createPushReplication(new URL(Constants.DB_SYNC_URL));
            pull.setContinuous(true);
            push.setContinuous(true);
            pull.start();
            push.start();
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error starting database replication", e);
        }

    }
}
