package com.madmen.hostr.data_adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.madmen.hostr.R;
import com.madmen.hostr.data_models.EventOption;

import java.util.Collections;
import java.util.List;

/**
 * Adapter class for the CreateEvent activity...
 * Legacy class for the placeholder EventOption class.
 *
 * @author Jay Whaley
 * @see com.madmen.hostr.data_models.EventOption
 */

public class EventOptionsAdapter extends RecyclerView.Adapter<EventOptionsAdapter.FeatureViewHolder> {

    private int mNumberOfItems;
    private OnListItemClickedListener listItemClickedListener;
    private List<EventOption> eventOption = Collections.EMPTY_LIST;

    /**
     * Uzi's old random interface... probably can ignore once fragments are in place for event Creation.
     */
    interface OnListItemClickedListener{
        void OnListItemClicked(int position);
    }

    /**
     * Default constructor for EventOptionsAdapter.
     * @param data - The List of checkboxes from the CreateEvent placeholders.
     */
    public EventOptionsAdapter(List data){
        eventOption = data;
    }

    /**
     * Creates a holder of View items.
     * Essentially just a roundabout way of inflating each layout item.
     *
     * @param parent - The parent Layout (ie R.layout.activity_create_event)
     * @param viewType - The type of View this resource is ((ViewType) R.id.xyz) [UNUSED]
     * @return a FeatureViewHolder for placeholder items.
     */
    @Override
    public FeatureViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutFromId = R.layout.custom_event_row;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutFromId, parent, false);
        return new FeatureViewHolder(view);
    }

    /**
     * Binds each holder object from onCreateViewHolder to an option in the RecyclerView.
     * These placeholders probably can get chucked, but the RecyclerView will be useful
     * to easily change out wanted vs unwanted options.
     *
     * @param holder - The FeatureViewHolder created in the previous method.
     * @param position - The layout portion this item is on.
     */
    @Override
    public void onBindViewHolder(FeatureViewHolder holder, int position) {
        EventOption current = eventOption.get(position);
        holder.description.setText(current.getTitle());
        holder.icon.setImageResource(current.getIcon());
    }

    /**
     * Required method for RecyclerViews.
     *
     * @return the number of items in the RecyclerView.
     */
    @Override
    public int getItemCount() {
        if (eventOption != null) {
            return eventOption.size();
        }
        else return 0;
    }


    /**
     * Binds each of the Features to their actual corresponding Views.
     *
     * @author Uzi
     */
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
}
