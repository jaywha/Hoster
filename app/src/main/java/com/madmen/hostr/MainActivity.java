package com.madmen.hostr;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.ui.FirebaseUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.storage.FirebaseStorage;
import com.madmen.hostr.activitieswithintents.fragment_package.GmapFragment;
import com.madmen.hostr.data_models.Event;

 /*
 * @startuml
 * title Hostr App - Class Diagram
 * class MainActivity {
 *  -mFab : FloatingActionButton
 *
 *  #onCreate(Bundle) : void
 *  +onBackPressed() : void
 *  +onCreateOptionsMenu(Menu) : boolean
 *  +onOptionsItemSelected(MenuItem) : boolean
 *  +onNavigationItemSelected(MenuItem) : boolean
 * }
 *
 * class CreateEvent {
 *  -mImageIcon : ImageView
 *  -mEventImage : ImageView
 *  -mCancelEventButton : Button
 *  -mEventTitleTextView : TextView
 *  -mEventHost : TextView
 *  -mSetEventName : EditText
 *  -mImageFromPhotoIcon : ImageView
 *  -mCreateEventButton : Button
 *  -mAdapter : EventOptionsAdapter
 *  -mRecyclerView : RecyclerView
 *
 *  #onCreate(Bundle) : void
 *  {static}+GetData() : List<EventFeaturesData>
 *  +shareApp(String) : void
 *  +onClickEventName(View) : void
 * }
 *
 * class EventOptionsAdapter {
 *  -mNumberOfItems : int
 *  -listItemClickedListener : OnListItemClickedListener
 *  -eventOption : List<EventFeaturesData> = Collections.EMPTY_LIST
 *  --
 *  ~EventOptionsAdapter(List) : EventOptionsAdapter
 *  ~onCreateViewHolder(ViewGroup, int) : FeatureViewHolder
 *  ~onBindViewHolder(FeatureViewHolder, int) : void
 *  ~getItemCounter() : int
 * }
 *
 * class FeatureViewHolder {
 *  ~description : TextView
 *  ~icon : ImageView
 *
 *  ~FeatureViewHolder(View) : FeatureViewHolder
 *  +onClick(View) : void
 * }
 *
 * interface OnListItemClickedListener {
 *  {abstract} ~OnListItemClicked(int) : void
 *  --
 * }
 *
 * MainActivity <-- CreateEvent
 * MainActivity <-- EventOptionsAdapter
 *
 * EventOptionsAdapter --|> OnListItemClickedListener
 * EventOptionsAdapter --> FeatureViewHolder
 * @enduml
 */

/**
 * Encompasses the core activity of the Hostr App.
 * Manages the fragments for options, settings, event creation, and other features.
 *
 * Original Source code from https://github.com/uzikid100/Hoster
 * @author Jay Whaley
 * @version alpha:0.0.1
 * @since 06/14/2017
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private FirebaseStorage mFirebaseStorage;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseUI mFirebaseUI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Toast toast = Toast.makeText(this, "Finding Events Near You!", Toast.LENGTH_SHORT);
        FloatingActionButton mFab = (FloatingActionButton) findViewById(R.id.fab);
        final Intent hover_intent = new Intent(this, EventGallery.class); // Default to EventGallery with Local Filter ON
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toast.show();
                startActivity(hover_intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        SmoothActionBarDrawerToggle toggle = new SmoothActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, new GmapFragment())
                .commit();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.create_event: {
                final Intent intent = new Intent(this, Event.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_gallery: {
                final Intent intent = new Intent(this, EventGallery.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_slideshow: {
                final Intent intent = new Intent(this, EventSlideshow.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_manage: {
                final Intent intent = new Intent(this, ManageProfile.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_share: {
                final Intent intent = new Intent(this, ShareEvent.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_send: {
                final Intent intent = new Intent(this, SendEvent.class);
                startActivity(intent);
                break;
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /**
     * Code from https://stackoverflow.com/a/29193661
     */
    private class SmoothActionBarDrawerToggle extends ActionBarDrawerToggle {

        private Runnable runnable;

        SmoothActionBarDrawerToggle(Activity activity, DrawerLayout drawerLayout, Toolbar toolbar, int openDrawerContentDescRes, int closeDrawerContentDescRes) {
            super(activity, drawerLayout, toolbar, openDrawerContentDescRes, closeDrawerContentDescRes);
        }

        @Override
        public void onDrawerOpened(View drawerView) {
            super.onDrawerOpened(drawerView);
            invalidateOptionsMenu();
        }
        @Override
        public void onDrawerClosed(View view) {
            super.onDrawerClosed(view);
            invalidateOptionsMenu();
        }
        @Override
        public void onDrawerStateChanged(int newState) {
            super.onDrawerStateChanged(newState);
            if (runnable != null && newState == DrawerLayout.STATE_IDLE) {
                runnable.run();
                runnable = null;
            }
        }

        public void runWhenIdle(Runnable runnable) {
            this.runnable = runnable;
        }
    }

}
