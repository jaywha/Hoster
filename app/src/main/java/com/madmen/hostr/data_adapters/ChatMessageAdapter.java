package com.madmen.hostr.data_adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.madmen.hostr.R;
import com.madmen.hostr.data_models.ChatMessage;

import java.util.List;

/**
 * Borrowed from the Udacity Course: Firebase in a Weekend (Android)
 * Please replace as deemed necessary or leave this disclaimer.
 *
 * @author Jay Whaley
 */
public class ChatMessageAdapter extends ArrayAdapter<ChatMessage> {

    /**
     * Default constructor for an ArrayAdapter for the ChatMessage class.
     *
     * @param context - The base context for the Activity calling the Adapter.
     * @param resource - The parent resource being used in the Adapter.
     * @param objects - The list of ChatMessage objects being passed through the Adapter.
     */
    public ChatMessageAdapter(Context context, int resource, List<ChatMessage> objects) {
        super(context, resource, objects);
    }

    /**
     * Adapts the desired Layout View to a ChatMessage Model.
     *
     * @param position - The list position of the item in question.
     * @param convertView - The View we wish to adapt to an ChatMessage Model.
     * @param parent - The parent layout containing these Views.
     * @return a View of this ChatMessage Model.
     */
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_message, parent, false);
        }

        //ImageView photoImageView = (ImageView) convertView.findViewById(R.id.photoImageView);
        TextView messageTextView = (TextView) convertView.findViewById(R.id.messageTextView);
        TextView authorTextView = (TextView) convertView.findViewById(R.id.nameTextView);

        messageTextView.setVisibility(View.VISIBLE);
        ChatMessage message = getItem(position);
            /*
            boolean isPhoto = (message.getPhoto_url() != null);
            if (isPhoto) {
                messageTextView.setVisibility(View.GONE);
                photoImageView.setVisibility(View.VISIBLE);
                Glide.with(photoImageView.getContext())
                        .load(message.getPhoto_url())
                        .into(photoImageView);
            } else {
                messageTextView.setVisibility(View.VISIBLE);
                photoImageView.setVisibility(View.GONE);
                messageTextView.setText(message.getText());
            }*/
        if(message != null) {
            authorTextView.setText(message.getDisplay_name());
            messageTextView.setText(message.getText());
        } else {
            authorTextView.setText("Anonymous");
            messageTextView.setText("Null Message");
        }

        return convertView;
    }
}
