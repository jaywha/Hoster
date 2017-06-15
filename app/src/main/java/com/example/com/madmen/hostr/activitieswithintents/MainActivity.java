package com.example.com.madmen.hostr.activitieswithintents;

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

import com.example.com.madmen.hostr.activitieswithintents.CreateEvent;
import com.example.hostr.activitieswithintents.R;
import com.example.hostr.activitieswithintents.fragment_package.GmapFragment;

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

    /*
    TODO
    Add recyclerView with eventOptions to the contentCreate View
    Add current location feature with marker and camera
    Simulate creating an event with mandatory fields
    Throw an error/handle mandatory eventFields not set... i.e. (location not set)
    Finish Preferences with udacity.
     */

    private FloatingActionButton mFab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Toast toast = Toast.makeText(this, "Finding Events Near You!", Toast.LENGTH_SHORT);
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toast.show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
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

        if (id == R.id.create_event) {
            final Intent intent = new Intent(this, CreateEvent.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
