package com.example.efradelos.divein.data;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by efradelos on 9/14/15.
 */
public abstract class ModelBase {
    private String mKey;

    @JsonIgnore
    public String getKey() {
        return mKey;
    }

    public void setKey(String key) {
        this.mKey = key;
    }
}
