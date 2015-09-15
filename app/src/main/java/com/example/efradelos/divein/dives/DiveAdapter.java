package com.example.efradelos.divein.dives;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.efradelos.divein.R;
import com.example.efradelos.divein.adapters.FirebaseListAdapter;
import com.example.efradelos.divein.data.Dive;
import com.firebase.client.Query;

/**
 * Created by efradelos on 8/31/15.
 */
public class DiveAdapter extends FirebaseListAdapter<Dive> {
    public DiveAdapter(Activity activity, int modelLayout, Query ref) {
        super(activity, Dive.class, modelLayout, ref);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View adapterView = super.getView(i, view, viewGroup);
        if(i == 0 || getCount() > 0 && !(getItem(i - 1).getCategory().equals(getItem(i).getCategory()))) {
            TextView category = (TextView)adapterView.findViewById(R.id.header);
            category.setVisibility(View.VISIBLE);
            category.setText(getItem(i).getCategory());
        }
        return adapterView;
    }

    @Override

    protected void populateView(View view, Dive dive) {
        ((TextView)view.findViewById(R.id.list_item_name)).setText(dive.getName());
    }
}
