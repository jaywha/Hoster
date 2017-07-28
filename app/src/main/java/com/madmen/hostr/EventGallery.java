package com.madmen.hostr;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.GridView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.madmen.hostr.data_adapters.EventGalleryAdapter;
import com.madmen.hostr.data_models.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * The EventGallery displays the Events on Firebase in a GridView.
 * TODO: Use Sai's Django API for calling production data.
 *
 * @author Jay Whaley
 */

public class EventGallery extends AppCompatActivity {
    private EventGalleryAdapter mGalleryAdapter;
    public static String message = "Work in Progress";
    private static final String TAG = "Hostr.EventGallery";
    private static int mNotificationId = 102;

    private GridView mEventGrid;

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseStorage mFirebaseStorage;
    private FirebaseAuth mFirebaseAuth;
    private Context context;

    /**
     * Main method for EventGallery.
     * This method attaches to the Firebase database to get Events created using CreateEvent.
     * This method sets up a notification for when new Events are created (also for currentUser for debug reasons)
     *
     * @param savedInstanceState - Android Default onCreate Parameter for Activity State Restoration
     * @see com.madmen.hostr.CreateEvent#onCreate(Bundle)
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_gallery);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();

        List<Event> events = new ArrayList<>();
        context = this;

        final EventGalleryAdapter mGalleryAdapter = new EventGalleryAdapter(getBaseContext(),R.id.event_gallery_list,events);
        //Add Test Event
        Event test_Event = new Event(true, "Testing This", "DEBUG", getResources().getDrawable(R.drawable.hostr_icon), "Hostr");
        mGalleryAdapter.add(test_Event);

        //Add Database Events
        DatabaseReference mEventAttributes = mFirebaseDatabase.getReference().child("event");
        mEventAttributes.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Event db_event = dataSnapshot.getValue(Event.class);
                mGalleryAdapter.add(db_event);

                FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();                
                boolean notify_condition = false;
                try {
                    notify_condition = (db_event != null && !db_event.getHost().isEmpty()) &&
                            (currentUser != null) &&
                            (currentUser.getDisplayName() != null) && !(currentUser.getDisplayName().isEmpty());/* &&
                            !(db_event.getHost().equals(currentUser.getDisplayName()));*/
                } catch (NullPointerException npe) {
                    Log.d(TAG, "Notification creation error"+npe.getLocalizedMessage());
                }

                if(notify_condition) {
                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(context)
                                    .setSmallIcon(R.drawable.hostr_icon)
                                    .setContentTitle("New Event")
                                    .setContentText(db_event.getTitle()+" posted by Hostr: "+db_event.getHost());
                    // Creates an explicit intent for an Activity in your app
                    Intent resultIntent = new Intent(context, MainActivity.class);

                    // The stack builder object will contain an artificial back stack for the
                    // started Activity.
                    // This ensures that navigating backward from the Activity leads out of
                    // your application to the Home screen.
                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                    // Adds the back stack for the Intent (but not the Intent itself)
                    stackBuilder.addParentStack(MainActivity.class);
                    // Adds the Intent that starts the Activity to the top of the stack
                    stackBuilder.addNextIntent(resultIntent);
                    PendingIntent resultPendingIntent =
                            stackBuilder.getPendingIntent(
                                    0,
                                    PendingIntent.FLAG_UPDATE_CURRENT
                            );
                    mBuilder.setContentIntent(resultPendingIntent);
                    NotificationManager mNotificationManager =
                            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                    // mNotificationId is a unique integer your app uses to identify the
                    // notification. For example, to cancel the notification, you can pass its ID
                    // number to NotificationManager.cancel().
                    mNotificationManager.notify(mNotificationId, mBuilder.build());
                }
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
        });

        mEventGrid = (GridView) findViewById(R.id.event_gallery_list);
        mEventGrid.setAdapter(mGalleryAdapter);

        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
    }

    /**
     * Default onPause method.
     */
    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * Default onResume method.
     */
    @Override
    protected void onResume() {
        super.onResume();
    }
}
