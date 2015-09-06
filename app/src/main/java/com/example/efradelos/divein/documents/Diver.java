package com.example.efradelos.divein.documents;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.couchbase.lite.Attachment;
import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Emitter;
import com.couchbase.lite.Mapper;
import com.couchbase.lite.Query;
import com.couchbase.lite.Revision;
import com.couchbase.lite.View;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Diver extends DocumentBase {
    private static final String LOG_TAG = "Diver";
    private static final String VIEW_NAME = "divers";
    private static final String DOC_TYPE = "dive";
    public static final String PROP_FIRST_NAME = "first_name";
    public static final String PROP_LAST_NAME = "last_name";
    public static final String PROP_YEAR = "year";
    public static final String PROP_AVATAR = "avatar";

    private String firstName;
    private String lastName;
    private String year;
    private Bitmap avatar;

    public static Diver createFromDocument(Document doc) {
        Diver diver = new Diver();
        diver.mId = doc.getId();
        diver.lastName = (String) doc.getProperty(PROP_FIRST_NAME);
        diver.firstName = (String) doc.getProperty(PROP_LAST_NAME);
        diver.year = (String) doc.getProperty(PROP_YEAR);
        Revision rev = doc.getCurrentRevision();
        com.couchbase.lite.Attachment att = rev.getAttachment(PROP_AVATAR);
        if (att != null) {
            try {
                diver.avatar = BitmapFactory.decodeStream(att.getContent());
            } catch(CouchbaseLiteException e) {
                Log.e(LOG_TAG, "Unable to retrieve avatar", e);
            }
        }
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

    public HashMap<String, Object> getProperties() {
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("type", DOC_TYPE);
        properties.put(PROP_FIRST_NAME, getFirstName());
        properties.put(PROP_LAST_NAME, getLastName());
        properties.put(PROP_YEAR, getYear());
        return properties;
    }

    public Iterable<Attachment> getAttachments() {
        if (avatar == null) return null;

        ArrayList<Attachment> attachments = new ArrayList<>();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        avatar.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] bitmapdata = bos.toByteArray();
        ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);

        attachments.add(new Attachment(PROP_AVATAR, "image/jpeg", bs));
        return attachments;
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
