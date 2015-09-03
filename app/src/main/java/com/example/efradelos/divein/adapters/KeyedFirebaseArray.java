package com.example.efradelos.divein.adapters;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

public class KeyedFirebaseArray<T> implements ChildEventListener {
    public interface OnChangedListener {
        enum EventType { Added, Changed, Removed, Moved }
        void onChanged(EventType type, int index, int oldIndex);
    }

    private final Class<T> mModelClass;
    private Query mQuery;
    private List<AbstractMap.SimpleEntry<String, T>> mSnapshots;
    private OnChangedListener mListener;

    public KeyedFirebaseArray(Query ref, Class<T> modelClass) {
        mModelClass = modelClass;
        mQuery = ref;
        mQuery.addChildEventListener(this);
        mSnapshots = new ArrayList<>();
    }

    public int getCount() {
        return mSnapshots.size();

    }

    public T getItem(int index) {
        return mSnapshots.get(index).getValue();
    }

    public String getKey(int index) {
        return mSnapshots.get(index).getKey();
    }

    public void cleanup() {
        mQuery.removeEventListener(this);
    }

    public void setOnChangedListener(OnChangedListener onChangedListener) {
        mListener = onChangedListener;
    }

    protected void notifyChangedListeners(OnChangedListener.EventType type, int index) {
        notifyChangedListeners(type, index, -1);
    }

    protected void notifyChangedListeners(OnChangedListener.EventType type, int index, int oldIndex) {
        if (mListener != null) {
            mListener.onChanged(type, index, oldIndex);
        }
    }

    private int getIndexForKey(String key) {
        int index = 0;
        for (AbstractMap.SimpleEntry<String, T> entry : mSnapshots) {
            if (entry.getKey().equals(key)) {
                return index;
            } else {
                index++;
            }
        }
        throw new IllegalArgumentException("Key not found");
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String key) {
        int index = 0;
        if (key != null) {
            index = getIndexForKey(key) + 1;
        }
        mSnapshots.add(index, new AbstractMap.SimpleEntry<>(dataSnapshot.getKey(), dataSnapshot.getValue(mModelClass)));
        notifyChangedListeners(OnChangedListener.EventType.Added, index);
    }

    @Override
    public void onChildChanged(DataSnapshot snapshot, String s) {
        int index = getIndexForKey(snapshot.getKey());
        mSnapshots.set(index, new AbstractMap.SimpleEntry<>(snapshot.getKey(), snapshot.getValue(mModelClass)));
        notifyChangedListeners(OnChangedListener.EventType.Changed, index);
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



}
