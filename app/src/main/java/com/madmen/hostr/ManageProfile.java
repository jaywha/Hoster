package com.madmen.hostr;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Allows users to create and edit their profiles.
 *
 * @author Jay Whaley
 */
public class ManageProfile extends AppCompatActivity {

    /**
     * Default onCreate
     *
     * @param savedInstanceState - Android Default onCreate Parameter for Activity State Restoration
     * @param persistentState - Android forced state.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        Toast.makeText(this,"WIP", Toast.LENGTH_SHORT).show();
    }
}
