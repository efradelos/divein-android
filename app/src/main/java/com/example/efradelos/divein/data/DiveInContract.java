package com.example.efradelos.divein.data;

import android.net.Uri;

/**
 * Created by efradelos on 8/31/15.
 */
public class DiveInContract {
    public static final String CONTENT_AUTHORITY = "com.example.android.sunshine.app";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_DIVES = "dives";
    public static final String PATH_DIVERS = "divers";

    public static final Uri DIVES_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_DIVES).build();
    public static final Uri DIVERS_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_DIVERS).build();

}
