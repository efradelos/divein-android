package com.example.efradelos.divein.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;

/**
 * Created by efradelos on 9/7/15.
 */
public class ImageUtils {
    public static final int THUMBNAIL_WIDTH = 100;
    public static final int THUMBNAIL_HEIGHT = 100;

    public static Bitmap extractThumbnail(Bitmap source) {
        return ThumbnailUtils.extractThumbnail(source, THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT);
    }
    public static Bitmap uriToBitmap(String uri) {
        int commaIndex = uri.indexOf(',');
        String decode = uri.substring(commaIndex);
        byte[] decodedString = Base64.decode(decode, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    public static String bitmapToUri(Bitmap image) {
        StringBuilder sb = new StringBuilder();
        sb.append("content:image/jpeg;base64,");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        sb.append(Base64.encodeToString(b,Base64.DEFAULT));
        return sb.toString();
    }
}
