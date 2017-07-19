package com.example.com.madmen.hostr.activitieswithintents;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Another class in ${PACKAGE}
 * Created by Jay Whaley on 7/19/2017.
 */

public class EventGallery extends AppCompatActivity {
    private EventGalleryAdapter mGalleryAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toast.makeText(getBaseContext(), "Work in Progress", Toast.LENGTH_LONG).show();
        finish();
    }
}
