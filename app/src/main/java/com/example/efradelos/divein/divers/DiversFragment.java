package com.example.efradelos.divein.divers;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.couchbase.lite.Database;
import com.example.efradelos.divein.Application;
import com.example.efradelos.divein.R;
import com.example.efradelos.divein.data.DiveInContract;
import com.example.efradelos.divein.documents.*;

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

        Database database = ((Application)getActivity().getApplication()).getDatabaseInstance();
        mDiverAdapter = new DiverAdapter(getActivity(), R.layout.list_item_diver, com.example.efradelos.divein.documents.Diver.getQuery(database).toLiveQuery());
        mListViewDivers = (ListView)rootView.findViewById(R.id.listview_divers);
        mListViewDivers.setAdapter(mDiverAdapter);
        mListViewDivers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Diver diver = Diver.createFromDocument(mDiverAdapter.getItem(position));
                Uri diverUri = DiveInContract.DIVERS_URI.buildUpon().appendPath(diver.getId()).build();
                Intent intent = new Intent(getActivity(), DiverActivity.class).setData(diverUri);
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
