package com.example.efradelos.divein.documents;

import android.graphics.Bitmap;
import android.util.Log;

import com.example.efradelos.divein.utils.ImageUtils;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;

public class Diver extends DocumentBase {
    public static final String TYPE = "diver";

    private String mAvatarEncoded;
    private Bitmap mAvatar;
    private String mFirstName;
    private String mLastName;
    private String mYear;

    public Diver() {}

    public Diver(String firstName, String lastName, String year) {
        mFirstName = firstName;
        mLastName = lastName;
        mYear = year;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @JsonIgnore
    public Bitmap getAvatar() { return mAvatar; }

    @JsonIgnore
    public void setAvatar(Bitmap avatar) {
        mAvatar = avatar;
        mAvatarEncoded = avatar != null ? ImageUtils.bitmapToUri(ImageUtils.extractThumbnail(avatar)) : null;
    }

    @JsonGetter("avatar")
    public String getAvatarEncoded() { return mAvatarEncoded; }

    @JsonSetter("avatar")
    public void setAvatarEncoded(String avatarEncoded) {
        mAvatarEncoded = avatarEncoded;
        mAvatar = avatarEncoded != null ? ImageUtils.uriToBitmap(avatarEncoded) : null;
    }

    public String getFirstName() {return mFirstName;}
    public void setFirstName(String firstName) {
        this.mFirstName = firstName;
    }

    public String getLastName() {
        return mLastName;
    }
    public void setLastName(String lastName) { mLastName = lastName; }

    public String getYear() {
        return mYear;
    }
    public void setYear(String year) {
        mYear = year;
    }

}
