package com.madmen.hostr.data_adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.madmen.hostr.EventView;
import com.madmen.hostr.R;
import com.madmen.hostr.data_models.Event;

import java.util.List;

/**
 * Adapter class for the EventGallery activity.
 * This class uses the Event model and controls how the data is Viewed in EventGallery.
 *
 * @author Jay Whaley
 */

public class EventGalleryAdapter extends ArrayAdapter<Event> {

    private Context mAdapterContext;

    /**
     * Default constructor for an ArrayAdapter for the Event class.
     *
     * @param context - The base context for the Activity calling the Adapter.
     * @param resource - The parent resource being used in the Adapter.
     * @param objects - The list of Event objects being passed through the Adapter.
     */
    public EventGalleryAdapter(@NonNull Context context, int resource, @NonNull List<Event> objects) {
        super(context, resource, objects);
        this.mAdapterContext = context;
    }

    /**
     * Adapts the desired Layout View to an Event Model.
     *
     * @param position - The list position of the item in question.
     * @param convertView - The View we wish to adapt to an Event Model.
     * @param parent - The parent layout containing these Views.
     * @return a View of this Event Model.
     */
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(mAdapterContext).inflate(R.layout.event_gallery_item, parent, false);
        }

        TextView eventTitleView = (TextView) convertView.findViewById(R.id.event_gallery_item_title);
        ImageButton eventImageView = (ImageButton) convertView.findViewById(R.id.event_gallery_item_image_button);

        Event event = getItem(position);

        if(event != null) {
            eventTitleView.setText(event.getTitle());
            eventImageView.setImageDrawable(event.getIcon());
            eventImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent view_event_intent = new Intent(mAdapterContext, EventView.class);
                    mAdapterContext.startActivity(view_event_intent);
                }
            });
        } else {
            eventTitleView.setText("Test");
            eventImageView.setImageDrawable(parent.getResources().getDrawable(R.drawable.hostr_icon));
        }

        return convertView;
    }
}
