package com.example.efradelos.divein.divers;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.efradelos.divein.R;
import com.example.efradelos.divein.adapters.FirebaseListAdapter;
import com.example.efradelos.divein.data.Diver;
import com.firebase.client.Query;

/**
 * Created by efradelos on 8/31/15.
 */
public class DiverAdapter extends FirebaseListAdapter<Diver> {
    private Bitmap mDefaultBitmap;

    public DiverAdapter(Activity activity, int modelLayout, Query ref) {
        super(activity, Diver.class, modelLayout, ref);
        mDefaultBitmap = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.profile);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View adapterView = super.getView(i, view, viewGroup);
        return adapterView;
    }

    @Override
    protected boolean match(Diver model, String query) {
        String hash = model.getFirstName() + model.getLastName();
        return hash.toLowerCase().contains(query);
    }

    @Override
    protected void populateView(View view, Diver diver) {
        ((TextView)view.findViewById(R.id.list_item_full_name)).setText(diver.getLastName() + ", " + diver.getFirstName());
        ((TextView)view.findViewById(R.id.list_item_year)).setText(diver.getYear());
        Bitmap avatar = diver.getAvatar();
        if (avatar == null) avatar = mDefaultBitmap;

        ImageView imageView = (ImageView)view.findViewById(R.id.list_item_avatar);
        BitmapDrawable drawable = (BitmapDrawable)imageView.getDrawable();
        if(drawable != null) {
            Bitmap oldBitmap = drawable.getBitmap();
            if (oldBitmap != mDefaultBitmap && !oldBitmap.isRecycled()) {
                oldBitmap.recycle();
            }

        }
        imageView.setImageBitmap(avatar);

    }
}
