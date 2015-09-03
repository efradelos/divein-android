package com.example.efradelos.divein.divers;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.efradelos.divein.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class DiverFragment extends Fragment {

    public DiverFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("EFX", getActivity().getIntent().getData().toString());
        View rootView = inflater.inflate(R.layout.fragment_diver, container, false);
        Button button = (Button)rootView.findViewById(R.id.save);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("EFX", "CLICK!");
            }
        });
        return rootView;

    }
}
