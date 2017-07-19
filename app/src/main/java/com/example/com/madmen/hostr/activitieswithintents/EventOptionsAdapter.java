package com.example.com.madmen.hostr.activitieswithintents;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hostr.activitieswithintents.NewEvent_Package.EventFeaturesData;
import com.example.hostr.activitieswithintents.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by com.madmen.hostr on 6/11/2017.
 */

class EventOptionsAdapter extends RecyclerView.Adapter<EventOptionsAdapter.FeatureViewHolder> {

    private int mNumberOfItems;
    private OnListItemClickedListener listItemClickedListener;
    private List<EventFeaturesData> eventOption = Collections.EMPTY_LIST;

    interface OnListItemClickedListener{
        void OnListItemClicked(int position);
    }


    EventOptionsAdapter(List data){
        eventOption = data;
    }

    @Override
    public FeatureViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutFromId = R.layout.custom_event_row;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutFromId, parent, false);
        return new FeatureViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FeatureViewHolder holder, int position) {
        EventFeaturesData current = eventOption.get(position);
        holder.description.setText(current.optionTitle);
        //holder.icon.setImageResource(current.icon);
    }

    @Override
    public int getItemCount() {
        if (eventOption != null) {
            return eventOption.size();
        }
        else return 0;
    }





    class FeatureViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView description;
        //ImageView icon;

        FeatureViewHolder(View itemView) {
            super(itemView);

            description = (TextView) itemView.findViewById(R.id.tv_custom_row_description);
            //icon = (ImageView) itemView.findViewById(R.id.iv_custom_row_image);
    }

        @Override
        public void onClick(View v) {
        }
    }
//
//    class EventFeaturesData {
//        int icon;
//        String optionTitle;
//    }
}
