package com.example.efradelos.divein.data;

import java.util.Map;

/**
 * Created by efradelos on 8/31/15.
 */
public class Dive extends ModelBase {
    String name;
    String category;
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

    public Map<String, Object> getDod() {
        return dod;
    }
    public void setDob(Map<String, Object> dod) {
        this.dod = dod;
    }


}
