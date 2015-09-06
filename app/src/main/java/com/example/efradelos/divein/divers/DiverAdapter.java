package com.example.efradelos.divein.divers;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.couchbase.lite.Document;
import com.couchbase.lite.LiveQuery;
import com.couchbase.lite.QueryRow;
import com.example.efradelos.divein.R;
import com.example.efradelos.divein.adapters.CouchListAdapter;
import com.example.efradelos.divein.documents.Diver;

public class DiverAdapter extends CouchListAdapter {
    public DiverAdapter(Activity activity, int modelLayout, LiveQuery query) {
        super(activity, modelLayout, query);
    }

    @Override
    protected void populateView(View view, Document doc) {
        Diver diver = Diver.createFromDocument(doc);
        ((TextView)view.findViewById(R.id.list_item_full_name)).setText(diver.getFirstName() + ", " + diver.getLastName());
        ((TextView)view.findViewById(R.id.list_item_year)).setText(diver.getYear());
//        String avatar = diver.getAvatar();
//        if (avatar != null) {
//            String avatarString = diver.getAvatar();
//            int commaIndex = avatarString.indexOf(',');
//            String decode = avatarString.substring(commaIndex);
//            byte[] decodedString = Base64.decode(decode, Base64.DEFAULT);
//            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//            ((ImageView) view.findViewById(R.id.list_item_avatar)).setImageBitmap(decodedByte);
//        }
    }
}
