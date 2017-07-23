package com.madmen.hostr.data_models;

import android.widget.ArrayAdapter;

/**
 * Another class in ${PACKAGE}
 * Created by Jay Whaley on 7/22/2017.
 */

public class ChatMessage {
    private String display_name;
    private String sender_id;
    private String text;
    private String timestamp;
    private String photo_url;

    public ChatMessage() {}

    public String getText() {
        return this.text;
    }

    public void setText(String txt) { this.text = txt; }

    public String getName() {
        return this.display_name;
    }

    public void setName(String name) { this.display_name = name; }

    public String getSender_id() {
        return this.sender_id;
    }

    public void setSender_id(String sender) {
        this.sender_id = sender;
    }

    public String getPhoto_url() {
        return this.photo_url;
    }

    public void setPhoto_url(String url) { this.photo_url = url; }

    public String getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(String ts) {
        this.timestamp = ts;
    }
}
