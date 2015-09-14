package com.example.efradelos.divein.divers;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.couchbase.lite.Document;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryEnumerator;
import com.couchbase.lite.QueryRow;
import com.example.efradelos.divein.R;
import com.example.efradelos.divein.adapters.CouchBaseListAdapter;

/**
 * Created by efradelos on 8/31/15.
 */
public class DiverAdapter extends CouchBaseListAdapter<com.example.efradelos.divein.documents.Diver> {
    public DiverAdapter(Activity activity, int modelLayout, Query query) {
        super(activity, com.example.efradelos.divein.documents.Diver.class, modelLayout, query);
    }


    @Override

    protected void populateView(View view, com.example.efradelos.divein.documents.Diver diver) {
        ((TextView)view.findViewById(R.id.list_item_full_name)).setText(diver.getLastName() + ", " + diver.getFirstName());
        ((TextView)view.findViewById(R.id.list_item_year)).setText(diver.getYear());
        Bitmap avatar = diver.getAvatar();
        if (avatar == null) avatar = BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.profile);

        ((ImageView) view.findViewById(R.id.list_item_avatar)).setImageBitmap(avatar);

    }
}
