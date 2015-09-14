package com.example.efradelos.divein.divers;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.couchbase.lite.Emitter;
import com.couchbase.lite.Mapper;
import com.example.efradelos.divein.R;
import com.example.efradelos.divein.data.DatabaseManager;
import com.example.efradelos.divein.documents.Diver;

import java.util.Map;

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

        com.couchbase.lite.View view = DatabaseManager.getView("divers", "1", new Mapper() {
            @Override
            public void map(Map<String, Object> document, Emitter emitter) {
                if (Diver.TYPE.equals(document.get("type"))) {
                    emitter.emit(document.get("type"), null);
                }
            }
        });

        mDiverAdapter = new DiverAdapter(getActivity(), R.layout.list_item_diver, view.createQuery());
        mListViewDivers = (ListView)rootView.findViewById(R.id.listview_divers);
        mListViewDivers.setAdapter(mDiverAdapter);
        mListViewDivers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Diver diver = (Diver)parent.getItemAtPosition(position);
            Uri uri = Uri.parse("content://com.example.efradelos.divein/divers/" + diver.getId());
            Intent intent = new Intent(getActivity(), DiverActivity.class).setData(uri);
            startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        mDiverAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mDiverAdapter.stopListening();
    }
}
