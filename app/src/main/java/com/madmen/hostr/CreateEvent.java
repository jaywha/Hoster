package com.madmen.hostr;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.madmen.hostr.data_models.Event;
import com.madmen.hostr.data_adapters.EventOptionsAdapter;
import com.madmen.hostr.data_models.EventOption;

import java.util.ArrayList;
import java.util.List;

/**
 * Inherited from Uzi.
 * Allows the user to completely create an event in one Activity.
 * TODO: add fragments for each of the event option inputs
 *
 * @author Jay Whaley
 */

public class CreateEvent extends AppCompatActivity {

    private static final int PIC_UPLOAD = 555;
    private static int NUM_TESTS = 0;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase mFirebaseDatabse;
    private FirebaseStorage mFirebaseStorage;

    private DatabaseReference mEventURLReference;
    private StorageReference mEventDataReference;

    private TextView mEventTitleTextView;
    private TextView mEventHost;
    private EditText mSetEventName;
    private ImageView mEventImage;
    private ImageView mImageIcon;

    /**
     * This method creates the entirety of the Event creation activity.
     * Originally by Uzi; edited by Jay W.
     *
     * @param savedInstanceState - Android Default onCreate Parameter for Activity State Restoration
     * @see com.madmen.hostr.data_models.Event
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();
        mFirebaseDatabse = FirebaseDatabase.getInstance();

        mEventTitleTextView = (TextView) findViewById(R.id.tv_eventName);
        mEventHost = (TextView) findViewById(R.id.event_host);
        mSetEventName = (EditText) findViewById(R.id.et_set_event_name);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.rv_event_options);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        EventOptionsAdapter mAdapter = new EventOptionsAdapter(GetData());
        mRecyclerView.setAdapter(mAdapter);

        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
        if(currentUser != null) {
            mEventURLReference = mFirebaseDatabse.getReference().child("event");
            mEventDataReference = mFirebaseStorage.getReference().child("event_images").child("Test_Event"+NUM_TESTS);
            mEventHost.setText(currentUser.getDisplayName());
        } else {
            mEventURLReference = null;
            mEventDataReference = null;
            mEventHost.setText(R.string.app_name);
        }

        mEventImage = (ImageView) findViewById(R.id.iv_event_image);
        Button mCancelEventButton = (Button) findViewById(R.id.cancel_createEvent_button);
        mImageIcon = (ImageView) findViewById(R.id.iv_insert_eventImage_icon);

        mCancelEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        mImageIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent accessPhotosIntent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                if(accessPhotosIntent.resolveActivity(getPackageManager())!= null){
                    startActivityForResult(accessPhotosIntent, PIC_UPLOAD);
                }
            }
        });


        mSetEventName.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if((event.getAction() == KeyEvent.ACTION_DOWN) ||
                        keyCode == KeyEvent.KEYCODE_BACK){

                    if(mSetEventName == null || mSetEventName.getText().toString().equals("")){
                        mEventTitleTextView.setText("~EVENT NAME");
                    }

                    String name = mSetEventName.getText().toString();

                    if(name.equals("")){
                        mEventTitleTextView.setText("~EVENT NAME");
                    }else{
                        mEventTitleTextView.setText(name);
                    }
                    mSetEventName.setVisibility(View.GONE);
                    mEventTitleTextView.setText(name);
                    mEventTitleTextView.setVisibility(View.VISIBLE);
                    return true;
                }
                return false;
            }
        });

        FloatingActionButton fabFinish = (FloatingActionButton) findViewById(R.id.fab_finish);
        fabFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEventURLReference.push().setValue(new Event(true, mSetEventName.getText().toString(), "TEST",
                        mEventImage.getDrawable(),mEventHost.getText().toString()));
                DatabaseReference mNumEvents = mFirebaseDatabse.getReference().child("num_events");
                mNumEvents.push().setValue(++NUM_TESTS);
                finish();
            }
        });
    }

    /**
     * This method captures when the user uploads an image via the photo icon.
     *
     * @param requestCode - The integer code used to start an Activity for a result.
     * @param resultCode - The resultant status code; usually OK or not.
     * @param data - The actual data returned from the called Activity.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PIC_UPLOAD && resultCode == RESULT_OK) {
            mEventDataReference.putFile(data.getData()).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri photo_uri = taskSnapshot.getDownloadUrl();
                    mEventImage.setImageURI(photo_uri);
                    mEventImage.setVisibility(View.VISIBLE);
                    mImageIcon.setVisibility(View.INVISIBLE);
                }
            });
        }
    }

    /**
     * A placeholder method for getting event data (and submitting it for Firebase upload.
     *
     * @return a List of EventOptions for placeholder usage.
     */
    public static List<EventOption> GetData(){
        List<EventOption> data = new ArrayList<>();

        String[] optDescription = {"SELECT VISIBILITY", "ENTER DATE AND TIME", "ENTER PRICE",
                                    "ENTER CAPACITY", "SET LOCATION", "CHOOSE EventOption TYPE"};
        int[] optIcons = {R.drawable.mask_flat_icon, R.drawable.calendar_icon_three, R.drawable.dollar,
                            R.drawable.followers_icon, R.drawable.find_event_icon, R.drawable.event_icon};

        for (int i = 0; i < optDescription.length && i < optIcons.length; i++) {
            EventOption current = new EventOption();
            current.setIcon(optIcons[i]);
            current.setTitle(optDescription[i]);
            data.add(current);
        }
        return data;
    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode){
//            case 1: if(requestCode == RESULT_OK){
//                Uri intentDataReturned = data.getData();
//                mImageFromPhotoIcon.setImageURI(intentDataReturned);
//                break;
//            }
//        }
//    }

    /**
     * Switch the visibility of the 2 text views for the title.
     *
     * @param view - The parent view (ie R.layout.activity_create_event)
     */
    public void onClickEventName(View view) {
        mEventTitleTextView.setVisibility(View.INVISIBLE);
        mSetEventName.setVisibility(View.VISIBLE);
    }
}
