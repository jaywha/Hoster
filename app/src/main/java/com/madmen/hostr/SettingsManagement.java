package com.madmen.hostr;

import android.app.Activity;
import android.os.Bundle;

import com.madmen.hostr.fragments.SettingsFragment;

/**
 * Allows the user to change their Preferences.
 *
 * @author Jay Whaley
 */

public class SettingsManagement extends Activity {

    /**
     * Default onCreate
     *
     * @param savedInstanceState - Android Default onCreate Parameter for Activity State Restoration
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
