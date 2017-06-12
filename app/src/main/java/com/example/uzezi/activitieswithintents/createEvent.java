package com.example.uzezi.activitieswithintents;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uzezi.activitieswithintents.NewEvent_Package.EventFeaturesData;
import com.example.uzezi.activitieswithintents.R;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

public class createEvent extends AppCompatActivity {

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

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
    }

    public static List<EventFeaturesData> GetData(){
        List<EventFeaturesData> data = new ArrayList<>();

        String[] optDescription = {"SELECT VISIBILITY", "ENTER DATE AND TIME", "ENTER PRICE",
                                    "ENTER CAPACITY", "SET LOCATION", "CHOOSE EVENT TYPE"};
        int[] optIcons = {R.drawable.mask_flat_icon, R.drawable.calendar_icon_three, R.drawable.dollar,
                            R.drawable.followers_icon, R.drawable.find_event_icon, R.drawable.event_icon};

        for (int i = 0; i < optDescription.length && i < optIcons.length; i++) {
            EventFeaturesData current = new EventFeaturesData();
            current.icon = optIcons[i];
            current.optionTitle = optDescription[i];
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


    public void onClickEventName(View view) {
        mEventTitleTextView.setVisibility(View.INVISIBLE);
        mSetEventName.setVisibility(View.VISIBLE);

    }
}
