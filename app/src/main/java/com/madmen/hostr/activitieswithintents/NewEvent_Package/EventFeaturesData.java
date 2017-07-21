package com.madmen.hostr.activitieswithintents.NewEvent_Package;

import java.util.Date;

public class EventFeaturesData {
    private boolean isVisible;
    private String optionTitle;
    private Date eventDate;
    private String time;
    private double price;
    private int capacity;
    private String location;
    private String eventType;
    private int icon;

    public String getTitle() {
        return optionTitle;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public void setTitle(String title) {
        this.optionTitle = title;
    }
}
