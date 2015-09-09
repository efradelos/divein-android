package com.example.efradelos.divein.dives;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.efradelos.divein.R;
import com.firebase.client.Firebase;

/**
 * A placeholder fragment containing a simple view.
 */
public class DivesFragment
        extends Fragment {

    private DiveAdapter mDiveAdapter;
    private ListView mListViewDives;

    public DivesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dives, container, false);

        Firebase.setAndroidContext(getActivity());

        Firebase ref = new Firebase("https://divein.firebaseio.com").child("dives");
        mDiveAdapter = new DiveAdapter(getActivity(), R.layout.list_item_dive, ref);
        mListViewDives = (ListView)rootView.findViewById(R.id.listview_dives);
        mListViewDives.setAdapter(mDiveAdapter);
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDiveAdapter.cleanup();
    }
}
