package com.example.efradelos.divein.divers;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
    public DiverAdapter(Activity activity, int modelLayout, Query ref) {
        super(activity, Diver.class, modelLayout, ref);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View adapterView = super.getView(i, view, viewGroup);
        return adapterView;
    }

    @Override

    protected void populateView(View view, Diver diver) {
        ((TextView)view.findViewById(R.id.list_item_full_name)).setText(diver.getLastName() + ", " + diver.getFirstName());
        ((TextView)view.findViewById(R.id.list_item_year)).setText(diver.getYear());
        Bitmap avatar = diver.getAvatar();
        if (avatar == null) avatar = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.profile);

        ((ImageView) view.findViewById(R.id.list_item_avatar)).setImageBitmap(avatar);

    }
}
