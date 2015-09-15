package com.example.efradelos.divein.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.efradelos.divein.data.ModelBase;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.util.ArrayList;

public abstract class FirebaseListAdapter<T extends ModelBase> extends BaseAdapter {

    private final Class<T> mModelClass;
    private FirebaseListener mListener;
    private Query mRef;
    private int mLayout;
    private Activity mActivity;
    private ArrayList<T> mModels;


    public FirebaseListAdapter(Activity activity, Class<T> modelClass, int modelLayout, Query ref) {
        mModelClass = modelClass;
        mLayout = modelLayout;
        mActivity = activity;
        mModels = new ArrayList<>();
        mListener = new FirebaseListener();
        mRef = ref;
        start();
    }

    public FirebaseListAdapter(Activity activity, Class<T> modelClass, int modelLayout, Firebase ref) {
        this(activity, modelClass, modelLayout, (Query) ref);
    }

    public void start() {
        mRef.addChildEventListener(mListener);
    }
    public void stop() {
        mRef.removeEventListener(mListener);
    }

    public Activity getActivity() { return mActivity; }

    @Override
    public int getCount() {
        return mModels.size();
    }

    @Override
    public T getItem(int i) { return mModels.get(i); }

    @Override
    public long getItemId(int i) {
        return mModels.get(i).hashCode();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = mActivity.getLayoutInflater().inflate(mLayout, viewGroup, false);
        }

        T model = getItem(i);

        // Call out to subclass to marshall this model into the provided view
        populateView(view, model);
        return view;
    }

    /**
     * Each time the data at the given Firebase location changes, this method will be called for each item that needs
     * to be displayed. The arguments correspond to the mLayout and mModelClass given to the constructor of this class.
     * <p>
     * Your implementation should populate the view using the data contained in the model.
     *
     * @param v     The view to populate
     * @param model The object containing the data used to populate the view
     */
    protected abstract void populateView(View v, T model);


    private class FirebaseListener implements ChildEventListener {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String key) {
            int index = 0;
            if (key != null) {
                index = getIndexForKey(key) + 1;
            }
            T model = dataSnapshot.getValue(mModelClass);
            model.setKey(dataSnapshot.getKey());
            mModels.add(index, model);
            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(DataSnapshot snapshot, String s) {
            int index = getIndexForKey(snapshot.getKey());
            T model = snapshot.getValue(mModelClass);
            model.setKey(snapshot.getKey());
            mModels.set(index, model);
            notifyDataSetChanged();
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {

        }

        private int getIndexForKey(String key) {
            int index = 0;
            for (T  entry : mModels) {
                if (entry.getKey().equals(key)) {
                    return index;
                } else {
                    index++;
                }
            }
            throw new IllegalArgumentException("Key not found");
        }
    }

}