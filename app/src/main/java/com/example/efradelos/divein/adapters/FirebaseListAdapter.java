package com.example.efradelos.divein.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.example.efradelos.divein.data.ModelBase;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.util.ArrayList;

public abstract class FirebaseListAdapter<T extends ModelBase> extends BaseAdapter implements Filterable {

    private final Class<T> mModelClass;
    private FirebaseListener mListener;
    private Query mRef;
    private int mLayout;
    private Activity mActivity;
    private ArrayList<T> mOrigModels = new ArrayList<>();
    private ArrayList<T> mFiltModels = new ArrayList<>();
    private Filter mFilter;
    private CharSequence mQuery = "";

    public FirebaseListAdapter(Activity activity, Class<T> modelClass, int modelLayout, Query ref) {
        mModelClass = modelClass;
        mLayout = modelLayout;
        mActivity = activity;
        mListener = new FirebaseListener();
        mFilter = new ModelFilter();
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
        return mFiltModels.size();
    }

    @Override
    public T getItem(int i) { return mFiltModels.get(i); }

    @Override
    public long getItemId(int i) {
        return mFiltModels.get(i).hashCode();
    }

    public void setQuery(CharSequence query) {
        mQuery = query;
        filter();
    }

    protected void filter() {
        getFilter().filter(mQuery);
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

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    protected abstract boolean match(T model, String query);
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
            mOrigModels.add(index, model);
            filter();
            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(DataSnapshot snapshot, String s) {
            int index = getIndexForKey(snapshot.getKey());
            T model = snapshot.getValue(mModelClass);
            model.setKey(snapshot.getKey());
            mOrigModels.set(index, model);
            filter();
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
            for (T  entry : mOrigModels) {
                if (entry.getKey().equals(key)) {
                    return index;
                } else {
                    index++;
                }
            }
            throw new IllegalArgumentException("Key not found");
        }
    }

    private class ModelFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();

            final ArrayList<T> nlist = new ArrayList<>();

            String filterString = constraint.toString().toLowerCase();

            for (T model : mOrigModels) {
                if (FirebaseListAdapter.this.match(model, filterString)) {
                    nlist.add(model);
                }
            }

            results.values = nlist;
            results.count = nlist.size();
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if(results.values != null) {
                mFiltModels = (ArrayList<T>) results.values;
                notifyDataSetChanged();
            }
        }

    }

}