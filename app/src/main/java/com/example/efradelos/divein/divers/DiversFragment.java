package com.example.efradelos.divein.divers;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.efradelos.divein.Constants;
import com.example.efradelos.divein.R;
import com.firebase.client.Firebase;

/**
 * A placeholder fragment containing a simple view.
 */
public class DiversFragment
        extends Fragment {

    private DiverAdapter mDiverAdapter;
    private ListView mListViewDivers;

    public DiversFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_divers, container, false);

        Firebase.setAndroidContext(getActivity());

        Firebase ref = Constants.FIREBASE_REF.child("divers");
        mDiverAdapter = new DiverAdapter(getActivity(), R.layout.list_item_diver, ref);
        mListViewDivers = (ListView)rootView.findViewById(R.id.listview_divers);
        mListViewDivers.setAdapter(mDiverAdapter);
        mListViewDivers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Diver diver = (Diver)parent.getItemAtPosition(position);
            Uri uri = Uri.parse("content://com.example.efradelos.divein/divers/" + diver.getKey());
            Intent intent = new Intent(getActivity(), DiverActivity.class).setData(uri);
            startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDiverAdapter.cleanup();
    }
}
