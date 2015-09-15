package com.example.efradelos.divein.dives;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.efradelos.divein.R;
import com.firebase.client.Firebase;
import com.firebase.client.Query;

/**
 * A placeholder fragment containing a simple view.
 */
public class DivesFragment
        extends ListFragment {

    private DiveAdapter mDiveAdapter;

    public DivesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        Firebase.setAndroidContext(getActivity());

        Query ref = new Firebase("https://divein.firebaseio.com").child("dives").orderByChild("category");
        mDiveAdapter = new DiveAdapter(getActivity(), R.layout.list_item_dive, ref);

        setListAdapter(mDiveAdapter);
        return rootView;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDiveAdapter.stop();
    }
}
