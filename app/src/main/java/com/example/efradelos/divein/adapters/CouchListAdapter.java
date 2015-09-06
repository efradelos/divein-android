package com.example.efradelos.divein.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.couchbase.lite.Document;
import com.couchbase.lite.LiveQuery;
import com.couchbase.lite.QueryEnumerator;

public abstract class CouchListAdapter extends BaseAdapter {
    private Activity mActivity;
    private LiveQuery mQuery;
    private QueryEnumerator mEnumerator;
    private int mLayout;

    public CouchListAdapter(Activity activity, int layout, LiveQuery query) {
        mQuery = query;
        mLayout = layout;
        mActivity = activity;

        query.addChangeListener(new LiveQuery.ChangeListener() {
            @Override
            public void changed(final LiveQuery.ChangeEvent event) {
                mEnumerator = event.getRows();
                notifyDataSetChanged();
            }
        });

        query.start();
    }

    public void cleanup() {
        mQuery.stop();
    }

    @Override
    public int getCount() {
        return mEnumerator != null ? mEnumerator.getCount() : 0;
    }

    @Override
    public Document getItem(int position) {
        return mEnumerator != null ? mEnumerator.getRow(position).getDocument() : null;
    }

    @Override
    public long getItemId(int position) {
        return mEnumerator.getRow(position).getSequenceNumber();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = mActivity.getLayoutInflater().inflate(mLayout, parent, false);
        }

        Document doc = getItem(position);

        // Call out to subclass to marshall this model into the provided view
        populateView(view, doc);
        return view;
    }

    protected abstract void populateView(View v, Document doc);

}
