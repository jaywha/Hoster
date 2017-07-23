package com.madmen.hostr;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.madmen.hostr.data_models.Event;
import com.madmen.hostr.data_adapters.EventOptionsAdapter;

import java.util.ArrayList;
import java.util.List;

public class CreateEvent extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseStorage mFirebaseStorage;

    private DatabaseReference mProfileReference;
    private StorageReference mEventImageReference;

    private ImageView mImageIcon;
    private ImageView mEventImage;
    private Button mCancelEventButton;
    private TextView mEventTitleTextView;
    private TextView mEventHost;
    private EditText mSetEventName;
    private ImageView mImageFromPhotoIcon;
    private Button mCreateEventButton;

    private EventOptionsAdapter mAdapter;
    private RecyclerView mRecyclerView;

    public String debugName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();

        mProfileReference = mFirebaseDatabase.getReference().child("profiles");
        mEventImageReference = mFirebaseStorage.getReference().child("event_images");

        mEventTitleTextView = (TextView) findViewById(R.id.tv_eventName);
        mEventHost = (TextView) findViewById(R.id.event_host);
        mSetEventName = (EditText) findViewById(R.id.et_set_event_name);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_event_options);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new EventOptionsAdapter(GetData());
        mRecyclerView.setAdapter(mAdapter);


//        mImageFromPhotoIcon = (ImageView) findViewById(R.id.image_from_photo_icon);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareApp("You're invited to " + mEventTitleTextView.getText().toString()
                + " " + mEventHost.getText().toString() + "\n\n" +
                "View the event now on Hostr: \n https//:hostr.com/download");
            }
        });

        mEventImage = (ImageView) findViewById(R.id.iv_event_image);
        mCancelEventButton =(Button) findViewById(R.id.cancel_createEvent_button);
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
                    startActivityForResult(accessPhotosIntent, 1);
                }
            }
        });


        mSetEventName.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        keyCode == KeyEvent.KEYCODE_ENTER){

                    if(mSetEventName == null || mSetEventName.equals("")){
                        mEventTitleTextView.setText("~EVENT NAME");
                    }

                    String name = mSetEventName.getText().toString();
                    debugName = name;

                    if(name == null || name.equals("")){
                        mEventTitleTextView.setText("~EVENT NAME");
                    }else{
                        mEventTitleTextView.setText(name);
                    }
                    mSetEventName.setVisibility(View.INVISIBLE);
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
                finish();
            }
        });
    }

    public static List<Event> GetData(){
        List<Event> data = new ArrayList<>();

        String[] optDescription = {"SELECT VISIBILITY", "ENTER DATE AND TIME", "ENTER PRICE",
                                    "ENTER CAPACITY", "SET LOCATION", "CHOOSE EVENT TYPE"};
        int[] optIcons = {R.drawable.mask_flat_icon, R.drawable.calendar_icon_three, R.drawable.dollar,
                            R.drawable.followers_icon, R.drawable.find_event_icon, R.drawable.event_icon};

        for (int i = 0; i < optDescription.length && i < optIcons.length; i++) {
            Event current = new Event();
            current.setIcon(optIcons[i]);
            current.setTitle(optDescription[i]);
            data.add(current);
        }
        return data;
    }

    public void shareApp (String linkToDownloadApp){
        ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(linkToDownloadApp)
                .setChooserTitle("Share Hostr with your friends!")
                .startChooser();
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

    public CreateEvent() {}

    public void onClickEventName(View view) {
        mEventTitleTextView.setVisibility(View.INVISIBLE);
        mSetEventName.setVisibility(View.VISIBLE);

    }
}
