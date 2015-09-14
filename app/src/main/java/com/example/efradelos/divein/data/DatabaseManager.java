package com.example.efradelos.divein.data;

import android.content.Context;
import android.util.Log;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Emitter;
import com.couchbase.lite.Manager;
import com.couchbase.lite.Mapper;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryEnumerator;
import com.couchbase.lite.QueryRow;
import com.couchbase.lite.View;
import com.couchbase.lite.android.AndroidContext;
import com.example.efradelos.divein.documents.Diver;
import com.example.efradelos.divein.documents.DocumentBase;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by efradelos on 9/10/15.
 */
public class DatabaseManager {
    public static final String LOG_TAG = DatabaseManager.class.getSimpleName();
    public static final String DB_NAME = "dive_in";


    public static Manager mManager;
    public static Database mDatabase;

    public static void initDb(Context context) {
        mManager = null;
        mDatabase = null;
        try {
            mManager = new Manager(new AndroidContext(context), Manager.DEFAULT_OPTIONS);
            mDatabase = mManager.getDatabase(DB_NAME);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error getting database", e);
            return;
        }
    }

    public static Manager getManagerInstance() {
        return mManager;
    }

    public static Database getDatabaseInstance() {
        return mDatabase;
    }


    public static Document createDocument(Map<String, Object> map) {
        Document document = mDatabase.createDocument();
        try {
            document.putProperties(map);
        } catch (CouchbaseLiteException e) {
            Log.e(LOG_TAG, "Error putting", e);
        }
        return document;
    }

    public static Document createDocument(DocumentBase document) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(
                PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);

        TypeReference<Map<String,Object>> typeRef
                = new TypeReference<Map<String,Object>>() {};
        Map<String,Object> map = mapper.convertValue(document, typeRef);
        return createDocument(map);
    }

    public static Document getDocument(String id) {
        return mDatabase.getDocument(id);
    }

    public static DocumentBase getDocument(String id, TypeReference klass) {
        Document doc = getDocument(id);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(
                PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);

        return mapper.convertValue(doc.getProperties(), klass);

    }

    public static View getView(String name, String version, Mapper mapper) {
        View view = null;
        view = mDatabase.getView(name);
        if (view.getMap() == null) {
            view.setMap(mapper, version);
        }

        return view;
    }

//    private static void createTestData() {
//        Diver d = new Diver("Lauren", "Zimbo", "Sophomore");
//        createDocument(d);
//        d = new Diver("Alissa", "Markson", "Junior");
//        createDocument(d);
//        d = new Diver("Latoya", "Mazarak", "Senior");
//        createDocument(d);
//    }

//    private static void printTestData() {
//        Query query = mDatabase.createAllDocumentsQuery();
//        try {
//            QueryEnumerator result = query.run();
//            for (QueryRow row: result) {
//                Log.d("EFX", (String)row.getDocument().getProperties().toString());
//            }
//        } catch(Exception e) {
//            Log.e(LOG_TAG, "What", e);
//        }
//    }
}
