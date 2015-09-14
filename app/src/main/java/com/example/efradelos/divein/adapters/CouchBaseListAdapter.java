package com.example.efradelos.divein.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.couchbase.lite.Document;
import com.couchbase.lite.LiveQuery;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryRow;
import com.example.efradelos.divein.documents.Diver;
import com.example.efradelos.divein.documents.DocumentBase;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.firebase.client.GenericTypeIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by efradelos on 9/10/15.
 */
public abstract class CouchBaseListAdapter<T extends DocumentBase> extends BaseAdapter implements LiveQuery.ChangeListener{

    protected Activity mActivity;
    protected Class<T> mClass;
    protected int mLayout;
    protected LiveQuery mLiveQuery;
    protected ArrayList<T> mData = new ArrayList<>();

    public CouchBaseListAdapter(Activity activity, Class<T> klass, int layout, Query query) {
        mClass = klass;
        mActivity = activity;
        mLayout = layout;
        mLiveQuery = query.toLiveQuery();
        mLiveQuery.addChangeListener(this);
    }

    public void startListening() {
        mLiveQuery.start();
    }

    public void stopListening() {
        mLiveQuery.stop();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public T getItem(int i) { return mData.get(i); }

    @Override
    public long getItemId(int i) {
        return mData.get(i).hashCode();
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

    public void changed(final LiveQuery.ChangeEvent event) {
        ((Activity) CouchBaseListAdapter.this.mActivity).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mData.clear();
                for (QueryRow row : event.getRows()) {
                    Document doc = row.getDocument();
                    Map<String, Object> map = doc.getProperties();
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.setPropertyNamingStrategy(
                            PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);

                    T model = mapper.convertValue(map, mClass);
                    mData.add(model);
                }
                notifyDataSetChanged();
            }
        });
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
