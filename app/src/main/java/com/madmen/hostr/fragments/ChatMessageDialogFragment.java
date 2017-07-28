package com.madmen.hostr.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.madmen.hostr.MainActivity;
import com.madmen.hostr.R;
import com.madmen.hostr.data_adapters.ChatMessageAdapter;
import com.madmen.hostr.data_models.ChatMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Another class in ${PACKAGE}
 * Created by Jay Whaley on 7/23/2017.
 */

public class ChatMessageDialogFragment extends DialogFragment {
    private static final String TAG = "Hostr.ChatFragment";
    private static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;
    private static final int mNotificationId = 101;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        List<ChatMessage> chatMessages = new ArrayList<>();
        final ChatMessageAdapter mMessageAdapter = new ChatMessageAdapter(getActivity(), R.id.messageListViewDialog, chatMessages);

        final FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();

        final FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
        final DatabaseReference mChatMessages = mFirebaseDatabase.getReference().child("8_messages");
        ChildEventListener mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ChatMessage chatMessage = dataSnapshot.getValue(ChatMessage.class);
                mMessageAdapter.add(chatMessage);
                boolean notify_condition = false;
                try {
                    notify_condition = (chatMessage != null && !chatMessage.getDisplay_name().isEmpty()) &&
                            (currentUser != null) &&
                            (currentUser.getDisplayName() != null) && !(currentUser.getDisplayName().isEmpty()) &&
                            !(chatMessage.getDisplay_name().equals(currentUser.getDisplayName()));
                } catch (NullPointerException npe) {
                    Log.d(TAG, "Notification creation error"+npe.getLocalizedMessage());
                }

                if(notify_condition) {
                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(getActivity())
                                    .setSmallIcon(R.drawable.hostr_icon)
                                    .setContentTitle("New Message")
                                    .setContentText(chatMessage.getText());
                    // Creates an explicit intent for an Activity in your app
                    Intent resultIntent = new Intent(getActivity(), MainActivity.class);

                    // The stack builder object will contain an artificial back stack for the
                    // started Activity.
                    // This ensures that navigating backward from the Activity leads out of
                    // your application to the Home screen.
                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(getActivity());
                    // Adds the back stack for the Intent (but not the Intent itself)
                    stackBuilder.addParentStack(MainActivity.class);
                    // Adds the Intent that starts the Activity to the top of the stack
                    stackBuilder.addNextIntent(resultIntent);
                    PendingIntent resultPendingIntent =
                            stackBuilder.getPendingIntent(
                                    0,
                                    PendingIntent.FLAG_UPDATE_CURRENT
                            );
                    mBuilder.setContentIntent(resultPendingIntent);
                    NotificationManager mNotificationManager =
                            (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

                    // mNotificationId is a unique integer your app uses to identify the
                    // notification. For example, to cancel the notification, you can pass its ID
                    // number to NotificationManager.cancel().
                    mNotificationManager.notify(mNotificationId, mBuilder.build());
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mChatMessages.addChildEventListener(mChildEventListener);

        @SuppressLint("InflateParams") View dialog_view = inflater.inflate(R.layout.message_dialog_layout, null);
        ListView mChatList = (ListView) dialog_view.findViewById(R.id.messageListViewDialog);
        final EditText mSendMessage = (EditText) dialog_view.findViewById(R.id.send_message);
        final Button mSendButton = (Button) dialog_view.findViewById(R.id.send_message_button);


        // Enable Send button when there's text to send
        mSendMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        mSendMessage.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});

        // Send button sends a message and clears the EditText
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentUser != null) {
                    ChatMessage friendlyMessage = new ChatMessage(mSendMessage.getText().toString(),
                            currentUser.getDisplayName(), "9_user");
                    mChatMessages.push().setValue(friendlyMessage);
                }

                // Clear input box
                mSendMessage.setText("");
            }
        });

        mChatList.setAdapter(mMessageAdapter);

        builder.setView(dialog_view);

        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setTitle(R.string.chat_message_dialog_title);

        return builder.create();
    }
}
