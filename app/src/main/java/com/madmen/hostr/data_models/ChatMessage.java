package com.madmen.hostr.data_models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
    public ChatMessage(String message, String username, String image_url) {
        this.text = message;
        this.display_name = username;
        this.photo_url = (image_url == null || image_url.isEmpty() ? "" : image_url);
        this.timestamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US).format(new Date());
    }

    public String getText() {
        return this.text;
    }

    public void setText(String txt) { this.text = txt; }

    public String getDisplay_name() {
        return this.display_name;
    }

    public void setDisplay_name(String display_name) { this.display_name = display_name; }

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
