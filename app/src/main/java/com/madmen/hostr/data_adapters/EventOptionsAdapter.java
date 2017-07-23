package com.madmen.hostr.data_adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.madmen.hostr.data_models.Event;
import com.madmen.hostr.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by com.madmen.hostr on 6/11/2017.
 */

public class EventOptionsAdapter extends RecyclerView.Adapter<EventOptionsAdapter.FeatureViewHolder> {

    private int mNumberOfItems;
    private OnListItemClickedListener listItemClickedListener;
    private List<Event> eventOption = Collections.EMPTY_LIST;

    interface OnListItemClickedListener{
        void OnListItemClicked(int position);
    }


    public EventOptionsAdapter(List data){
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
        Event current = eventOption.get(position);
        holder.description.setText(current.getTitle());
        holder.icon.setImageResource(current.getIcon());
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
        ImageView icon;

        FeatureViewHolder(View itemView) {
            super(itemView);

            description = (TextView) itemView.findViewById(R.id.tv_custom_row_description);
            icon = (ImageView) itemView.findViewById(R.id.iv_custom_row_image);
    }

        @Override
        public void onClick(View v) {
        }
    }
//
//    class Event {
//        int icon;
//        String optionTitle;
//    }
}
