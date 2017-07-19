package com.example.com.madmen.hostr.activitieswithintents;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.example.hostr.activitieswithintents.NewEvent_Package.EventFeaturesData;

import java.util.Collections;
import java.util.List;

/**
 * Another class in ${PACKAGE}
 * Created by Jay Whaley on 7/19/2017.
 */

class EventGalleryAdapter extends RecyclerView.Adapter<EventOptionsAdapter.FeatureViewHolder> {
    private int mNumberOfEvents;
    private EventOptionsAdapter.OnListItemClickedListener listItemClickedListener;
    private List eventOption = Collections.EMPTY_LIST;

    interface OnListItemClickedListener{
        void OnListItemClicked(int position);
    }

    @Override
    public EventOptionsAdapter.FeatureViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(EventOptionsAdapter.FeatureViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
