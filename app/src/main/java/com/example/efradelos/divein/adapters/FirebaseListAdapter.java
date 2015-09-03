package com.example.efradelos.divein.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.firebase.client.Firebase;
import com.firebase.client.Query;

public abstract class FirebaseListAdapter<T> extends BaseAdapter {

    private final Class<T> mModelClass;
    protected int mLayout;
    protected Activity mActivity;
    protected KeyedFirebaseArray<T> mSnapshots;


    public FirebaseListAdapter(Activity activity, Class<T> modelClass, int modelLayout, Query ref) {
        mModelClass = modelClass;
        mLayout = modelLayout;
        mActivity = activity;
        mSnapshots = new KeyedFirebaseArray<T>(ref, modelClass);
        mSnapshots.setOnChangedListener(new KeyedFirebaseArray.OnChangedListener() {
            @Override
            public void onChanged(EventType type, int index, int oldIndex) {
                notifyDataSetChanged();
            }
        });
    }

    public FirebaseListAdapter(Activity activity, Class<T> modelClass, int modelLayout, Firebase ref) {
        this(activity, modelClass, modelLayout, (Query) ref);
    }

    public void cleanup() {
        mSnapshots.cleanup();
    }

    @Override
    public int getCount() {
        return mSnapshots.getCount();
    }

    @Override
    public T getItem(int i) { return mSnapshots.getItem(i); }

    @Override
    public long getItemId(int i) {
        return mSnapshots.getKey(i).hashCode();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = mActivity.getLayoutInflater().inflate(mLayout, viewGroup, false);
        }

        T model = mSnapshots.getItem(i);

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
}