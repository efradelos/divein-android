package com.example.efradelos.divein.divers;

import android.app.ListFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.efradelos.divein.Constants;
import com.example.efradelos.divein.R;
import com.example.efradelos.divein.Searchable;
import com.example.efradelos.divein.data.Diver;
import com.firebase.client.Firebase;

/**
 * A placeholder fragment containing a simple view.
 */
public class DiversFragment
        extends ListFragment
        implements Searchable {

    private DiverAdapter mDiverAdapter;

    public DiversFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        Firebase.setAndroidContext(getActivity());

        Firebase ref = Constants.FIREBASE_REF.child("divers");
        mDiverAdapter = new DiverAdapter(getActivity(), R.layout.list_item_diver, ref);
        setListAdapter(mDiverAdapter);

        return rootView;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Diver diver = (Diver) l.getItemAtPosition(position);
        Uri uri = Uri.parse("content://com.example.efradelos.divein/divers/" + diver.getKey());
        Intent intent = new Intent(getActivity(), DiverActivity.class).setData(uri);
        startActivity(intent);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDiverAdapter.stop();
    }

    @Override
    public void search(CharSequence query) {
        if(mDiverAdapter != null) mDiverAdapter.setQuery(query);
    }
}
