package com.madmen.hostr;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.madmen.hostr.data_models.Event;

/**
 * EventView allows the user to see an event.
 * Mirrors the CreateEvent view for UI familiarity.
 *
 * @author Jay Whaley
 */

public class EventView extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase mFirebaseDatabse;
    private FirebaseStorage mFirebaseStorage;

    private TextView mEventTitleTextView;
    private TextView mEventHost;
    private ImageView mEventImage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabse = FirebaseDatabase.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();

        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();

        DatabaseReference mEventData = mFirebaseDatabse.getReference().child("event").child("Test_Event");
        final StorageReference mEventPhoto = mFirebaseStorage.getReference().child("Test_event"+0); // Integer for number of tests from create event

        //TODO: if (currentUser.getDisplayName().equals(event_hostr)) {show editable event;}
        setContentView(R.layout.activity_create_event);

        mEventTitleTextView = (TextView) findViewById(R.id.tv_eventName);
        mEventHost = (TextView) findViewById(R.id.event_host);
        mEventImage = (ImageView) findViewById(R.id.iv_event_image);

        mEventData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Event db_event = dataSnapshot.getValue(Event.class);
                mEventTitleTextView.setText(db_event.getTitle());
                mEventHost.setText(db_event.getHost());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mEventPhoto.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                mEventImage.setImageURI(uri);
            }
        });
    }
}
