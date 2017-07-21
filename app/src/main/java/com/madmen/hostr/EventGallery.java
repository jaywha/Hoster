package com.madmen.hostr;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.madmen.hostr.data_adapters.EventGalleryAdapter;

/**
 * Another class in ${PACKAGE}
 * Created by Jay Whaley on 7/19/2017.
 */

public class EventGallery extends AppCompatActivity {
    private EventGalleryAdapter mGalleryAdapter;
    public static String message = "Work in Progress";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
        finish();
    }
}
