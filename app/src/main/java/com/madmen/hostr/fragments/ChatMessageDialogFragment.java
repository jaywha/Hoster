package com.madmen.hostr.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
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
    private static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        List<ChatMessage> chatMessages = new ArrayList<>();
        final ChatMessageAdapter mMessageAdapter = new ChatMessageAdapter(getActivity(), R.id.messageListViewDialog, chatMessages);

        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();

        final FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
        final DatabaseReference mChatMessages = mFirebaseDatabase.getReference().child("9_messages");
        ChildEventListener mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ChatMessage chatMessage = dataSnapshot.getValue(ChatMessage.class);
                mMessageAdapter.add(chatMessage);
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
                            currentUser.getDisplayName(), null);
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
