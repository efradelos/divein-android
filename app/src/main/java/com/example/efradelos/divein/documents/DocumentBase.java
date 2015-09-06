package com.example.efradelos.divein.documents;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.UnsavedRevision;

import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;

public abstract class DocumentBase {
    public class Attachment {
        public String name;
        public String contentType;
        public InputStream stream;

        public Attachment(String name, String contentType, InputStream stream) {
            this.name = name;
            this.contentType = contentType;
            this.stream = stream;
        }
    }

    protected String mId;

    public String getId() { return mId; }
    public void setId(String id) { mId = id; }

    abstract HashMap<String, Object> getProperties();
    protected Iterable<Attachment> getAttachments() { return null; };


    public String save(Database database) throws CouchbaseLiteException {
        Document document = getId() == null ? database.createDocument() : database.getDocument(getId());

        UnsavedRevision rev = document.createRevision();
        HashMap<String, Object> props = getProperties();
        rev.setProperties(props);
        Iterable<Attachment> attachments = getAttachments();
        if(attachments != null) {
            for(Attachment attachment: attachments) {
                rev.setAttachment(attachment.name, attachment.contentType, attachment.stream);
            }
        }
        rev.save();
        return document.getId();
    }

}
