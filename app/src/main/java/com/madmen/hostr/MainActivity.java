package com.madmen.hostr;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.madmen.hostr.fragments.GmapFragment;
import com.madmen.hostr.data_adapters.ChatMessageAdapter;
import com.madmen.hostr.data_models.ChatMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
 *  {static}+GetData() : List<Event>
 *  +shareApp(String) : void
 *  +onClickEventName(View) : void
 * }
 *
 * class EventOptionsAdapter {
 *  -mNumberOfItems : int
 *  -listItemClickedListener : OnListItemClickedListener
 *  -eventOption : List<Event> = Collections.EMPTY_LIST
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

    private static final int RC_SIGN_IN = 1;
    private static final String TAG = "Hostr.MainActivity";

    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseStorage mFirebaseStorage;

    private DatabaseReference mMessagesDatabaseReference;
    private StorageReference mChatPhotosStorageReference;

    private ChatMessageAdapter mMessageAdapter;

    private ChildEventListener mChildEventListener;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        List<ChatMessage> chatMessages = new ArrayList<>();
        mMessageAdapter = new ChatMessageAdapter(this, R.layout.item_message, chatMessages);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();

        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("messages");
        mChatPhotosStorageReference = mFirebaseStorage.getReference().child("chat_photos");

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

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //user is signed in
                    onSignedInInitialize(user.getDisplayName());
                } else {
                    onSingedOutCleanup();
                    //user is signed out
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(
                                            Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                                                    new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build(),
                                                    new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build()
                                            )
                                    )
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == ResultCodes.OK) {
                FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
                onSignedInInitialize(currentUser.getEmail());
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    onBackPressed();
                    finish();
                }
                try {
                    int err_lvl = response.getErrorCode();
                    if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                        Log.d(TAG, "Sign-in response got a no network error code --> ERR_"+err_lvl);
                    }
                    if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                        Log.d(TAG, "Sign-in response got an unknown error code --> ERR_"+err_lvl);
                    }
                } catch (NullPointerException npe) {
                    Log.d(TAG, "Sign-in response was null: "+npe.getLocalizedMessage());
                    finish();
                }
            }
        }
    }

    private void onSignedInInitialize(String email) {
        //do Initial User Setup
        TextView user_email_text = (TextView) findViewById(R.id.user_email);
        user_email_text.setText(email);
        attachDatabaseReadListener();
    }

    private void onSingedOutCleanup() {
        //do Sign-Out and prepare app for Sign-In
        Toast.makeText(this, "Signing Out of Hostr...", Toast.LENGTH_LONG).show();
        detachDatabaseReadListener();
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        //Check for sign-in
        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
        if (currentUser == null) {
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setIsSmartLockEnabled(false)
                            .setAvailableProviders(
                                    Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                                            new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build(),
                                            new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build()
                                    )
                            )
                            .build(),
                    RC_SIGN_IN);
        }
        //updateUI accordingly
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
        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_sign_out:
                mFirebaseAuth.signOut();
                onSingedOutCleanup();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.create_event: {
                final Intent intent = new Intent(this, CreateEvent.class);
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
                DialogFragment chatFragment = new ChatMessageDialogFragment();
                chatFragment.show(getFragmentManager(), "messageDialog");
                break;
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void attachDatabaseReadListener() {
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    ChatMessage chatMessage = dataSnapshot.getValue(ChatMessage.class);
                    mMessageAdapter.add(chatMessage);
                }

                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                }

                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }

                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                public void onCancelled(DatabaseError databaseError) {
                }
            };
            mMessagesDatabaseReference.addChildEventListener(mChildEventListener);
        }
    }

    private void detachDatabaseReadListener() {
        if(mChildEventListener != null) {
            mMessagesDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }

    public static class ChatMessageDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            LayoutInflater inflater = getActivity().getLayoutInflater();
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            List<ChatMessage> chatMessages = new ArrayList<>();
            final ChatMessageAdapter mMessageAdapter = new ChatMessageAdapter(getActivity(), R.id.messageListViewDialog, chatMessages);
            FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference mChatMessages = mFirebaseDatabase.getReference().child("8_messages");
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

            View dialog_view = inflater.inflate(R.layout.message_dialog_layout, null);
            ListView mChatList = (ListView) dialog_view.findViewById(R.id.messageListViewDialog);

            mChatList.setAdapter(mMessageAdapter);

            builder.setView(dialog_view);

            builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            return builder.create();
        }
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
