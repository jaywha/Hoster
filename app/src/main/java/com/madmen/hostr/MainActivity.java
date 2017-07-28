package com.madmen.hostr;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ShareCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.android.gms.internal.kx;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.madmen.hostr.fragments.ChatMessageDialogFragment;
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

    private ChatMessageAdapter mMessageAdapter;

    private FirebaseAuth.AuthStateListener mAuthStateListener;

    /**
     * This onCreate method encompasses all that is the starting point of the Hostr app.
     * This method will create an AuthState Listener for signing in using various providers.
     * This method will create an app drawer to navigate the sea of events and the profiles that host them.
     * Also houses a global chat that only the developers have access to currently.
     *
     * @param savedInstanceState - Android Default onCreate Parameter for Activity State Restoration
     * @see com.madmen.hostr.data_models.Event
     * @see com.madmen.hostr.data_models.Profile
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        List<ChatMessage> chatMessages = new ArrayList<>();
        mMessageAdapter = new ChatMessageAdapter(this, R.layout.item_message, chatMessages);

        mFirebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();

        final Toast toast = Toast.makeText(this, (currentUser == null ? "9_user" : currentUser.getUid())
                +" | "+(currentUser != null ? currentUser.getProviderId() : "Provider_ID"), Toast.LENGTH_SHORT);
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
                FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
                if (currentUser != null) {
                    //user is signed in
                    onSignedInInitialize(currentUser.getEmail());
                } else {
                    //user is signed out
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setTheme(R.style.AppTheme)
                                    .setLogo(R.drawable.hostr_icon)
                                    /* TODO Add terms and privacy policies to AuthUI
                                    .setTosUrl("TOS_URL")
                                    .setPrivacyPolicyUrl("PRIVACY_URL")*/
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

    /**
     * Catches activity results from startActivityForResult requests.
     * Specifically used for Firebase Auth UI Sign-in.
     *
     * @param requestCode - The integer code used to start an Activity for a result.
     * @param resultCode - The resultant status code; usually OK or not.
     * @param data - The actual data returned from the called Activity.
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == ResultCodes.OK) {
                FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
                if (currentUser != null) {
                    onSignedInInitialize(currentUser.getEmail());
                    Log.d(TAG, "Current User ID: "+currentUser.getUid() );
                }
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    onBackPressed();
                } else {
                    int err_lvl = response.getErrorCode();
                    if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                        Log.d(TAG, "Sign-in response got a no network error code --> ERR_" + err_lvl);
                    }
                    if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                        Log.d(TAG, "Sign-in response got an unknown error code --> ERR_" + err_lvl);
                    }
                }
            }
        }
    }

    /**
     * This method should be beefed up to accommodate other Sing-In actions.
     *
     * @param email - The currentUser's email address pulled from Firebase.
     */
    private void onSignedInInitialize(String email) {
        //do Initial User Setup
        TextView user_email_text = (TextView) findViewById(R.id.user_email);
        if(user_email_text != null) {
            user_email_text.setText(email);
        }
    }

    /**
     * Handles when the user presses the Back button.
     * Especially useful for the DrawerLayout.
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * This is the hamburger (or 3 dots at the top right) menu.
     * For now, only inflates with a Sign-Out and Settings option.
     *
     * @param menu - The actual hamburger menu item itself
     * @return boolean value which states if the menu was successfully created.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * This is the hamburger menu's switch case method.
     * Any options added need to have some sort of action here
     *
     * @param item - The item the user chose from the hamburger menu.
     * @return boolean value which states if the menu item successfully ran
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                // TODO link to com.madmen.hostr.SettingsManagement
                return true;
            case R.id.action_sign_out:
                mFirebaseAuth.signOut();
                Toast.makeText(this, "Signing Out of Hostr...", Toast.LENGTH_LONG).show();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This is the switch case method for selecting drawer items.
     * Any new items made in R.layout.content
     *
     * @param item - The drawer item selected
     * @return boolean value for successful completion of the selcted drawer item
     */
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
            case R.id.nav_manage: {
                final Intent intent = new Intent(this, ManageProfile.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_share: {
                String linkToDownloadApp = "Check out events now on Hostr!\nhttps//:hostr.com/download";

                ShareCompat.IntentBuilder.from(this)
                        .setType("text/plain")
                        .setText(linkToDownloadApp)
                        .setChooserTitle("Share Hostr with your friends!")
                        .startChooser();
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

    /**
     * When paused, the app should remove its AuthStateListener to avoid eating resources.
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
        mMessageAdapter.clear();
    }

    /**
     * When resumed, the app needs to re-add the AuthStateListener to ensure users are signed in.
     */
    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    /**
     * On first starting, the app will force the user to log in.
     * TODO: Ensure that only 1 Login screen appears.
     */
    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
        if(currentUser == null) {
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setIsSmartLockEnabled(false)
                            .setTheme(R.style.AppTheme)
                            .setLogo(R.drawable.hostr_icon)
                                    /* TODO Add terms and privacy policies to AuthUI
                                    .setTosUrl("TOS_URL")
                                    .setPrivacyPolicyUrl("PRIVACY_URL")*/
                            .setAvailableProviders(
                                    Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                                            new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build(),
                                            new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build()
                                    )
                            )
                            .build(),
                    RC_SIGN_IN);
        } // else skip to main layout
    }

    /**
     * Code from https://stackoverflow.com/a/29193661
     * TODO: Remove when DrawerToggle can operate smoothly (ie, lower res drawables)
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
