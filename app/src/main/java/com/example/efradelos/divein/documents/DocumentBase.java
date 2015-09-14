package com.example.efradelos.divein.documents;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Created by efradelos on 9/10/15.
 */
@JsonIgnoreProperties({ "_rev" })
public abstract class DocumentBase {
    private String id;

    public abstract String getType();

    public void setType(String type) {}

    @JsonGetter("_id")
    public @JsonIgnore String getId() { return id; }


    @JsonSetter("_id")
    public void setId(String id) {
        this.id = id;
    }
}
