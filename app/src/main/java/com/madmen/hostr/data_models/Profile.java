package com.madmen.hostr.data_models;

import android.widget.ImageView;

import java.util.Date;

/**
 * Model for Profile Data
 * Created by Jay Whaley on 7/19/2017.
 */

class Profile {
    // Public Information
    private String username = "";
    private String fullname = "";
    private String description = "";
    private ImageView profilePic;

    // Private Information
    private String email = "";
    private Date birthday = new Date(111L);
    private String gender = "";
    private int phone_num = 0;

    // Management
    private Profile[] friend_list;

    public Profile(String unameIn, String fnameIn) {
        username = unameIn;
        fullname = fnameIn;
    }

    public String getUsername() {
        return username;
    }

    public String getFullname() {
        return fullname;
    }
}
