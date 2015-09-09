package com.example.efradelos.divein.divers;

import android.graphics.Bitmap;
import android.util.Log;

import com.example.efradelos.divein.utils.ImageUtils;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;

public class Diver {
    public static final String FIREBASE_PATH = "divers";

    private String mAvatarEncoded;
    private Bitmap mAvatar;
    private String mFirstName;
    private String mKey;
    private String mLastName;
    private String mYear;

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

    public String getKey() {
        return mKey;
    }
    public void setKey(String key) {
        mKey = key;
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
