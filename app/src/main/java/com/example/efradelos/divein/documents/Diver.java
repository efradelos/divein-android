package com.example.efradelos.divein.documents;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Emitter;
import com.couchbase.lite.Mapper;
import com.couchbase.lite.Query;
import com.couchbase.lite.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Diver {
    private static final String VIEW_NAME = "divers";
    private static final String DOC_TYPE = "dive";
    public static final String PROP_FIRST_NAME = "first_name";
    public static final String PROP_LAST_NAME = "last_name";
    public static final String PROP_YEAR = "year";

    private String id;
    private String firstName;
    private String lastName;
    private String year;
    private Bitmap avatar;

    public static Diver createFromDocument(Document doc) {
        Diver diver = new Diver();
        diver.id = doc.getId();
        diver.lastName = (String) doc.getProperty(PROP_FIRST_NAME);
        diver.firstName = (String) doc.getProperty(PROP_LAST_NAME);
        diver.year = (String) doc.getProperty(PROP_YEAR);
        return diver;
    }

    public static Query getQuery(Database database) {
        View view = database.getView(VIEW_NAME);
        if (view.getMap() == null) {
            Mapper map = new Mapper() {
                @Override
                public void map(Map<String, Object> document, Emitter emitter) {
                    if (DOC_TYPE.equals(document.get("type"))) {
                        java.util.List<Object> keys = new ArrayList<>();
                        keys.add(document.get("last_name"));
                        keys.add(document.get("first_name"));
                        keys.add(document.get("year"));
                        emitter.emit(keys, null);
                    }
                }
            };
            view.setMap(map, "1");
        }

        Query query = view.createQuery();

        return query;
    }

    public static Document create(Database database, String first_name, String last_name, String year) throws CouchbaseLiteException {
        Document document = database.createDocument();

        Map<String, Object> properties = new HashMap<>();
        properties.put("type", DOC_TYPE);
        properties.put(PROP_FIRST_NAME, first_name);
        properties.put(PROP_LAST_NAME, last_name);
        properties.put(PROP_YEAR, year);

        // Save the properties to the document
        document.putProperties(properties);

        return document;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Bitmap getAvatar() {
        return avatar;
    }

    public void setAvatar(Bitmap avatar) {
        this.avatar = avatar;
    }
}
