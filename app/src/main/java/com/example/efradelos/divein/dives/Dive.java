package com.example.efradelos.divein.dives;

import java.util.Map;

/**
 * Created by efradelos on 8/31/15.
 */
public class Dive {
    String name;
    String category;
    String key;
    Map<String, Object> dod;

    public Dive() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Map<String, Object> getDod() {
        return dod;
    }

    public void setDob(Map<String, Object> dod) {
        this.dod = dod;
    }


}
