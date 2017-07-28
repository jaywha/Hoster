package com.madmen.hostr.data_models;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Event model
 * Instance variables determine the JSON tree for an Event on Firebase
 * TODO: Match this model's data with Sai's Django API
 *
 * @author Jay Whaley
 */

public class Event {
    private boolean isVisible;
    private String title;
    private Date eventDate;
    private String time;
    private double price;
    private int capacity;
    private String location;
    private String eventType;
    private Drawable icon;
    private String host;

    /**
     * Default constructor. Required by TaskSnapshot calls.
     */
    public Event() {}

    /**
     * Special constructor for pushing an Event to the database.
     *
     * @param visibility - Is the Event public?
     * @param title - Event Title
     * @param type - Event Type
     * @param event_image - Image for the Event (URL?)
     * @param hoster - The Host(ess) of the Event
     */
    public Event(boolean visibility, String title, String type, Drawable event_image, String hoster) {
        this.title = title;
        this.eventType = type;
        this.isVisible = visibility;
        this.host = hoster;
        this.icon = event_image;
    }

    /**
     * Get this Event's Title.
     *
     * @return Event Title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get this Event's Icon.
     *
     * @return Event Icon.
     */
    public Drawable getIcon() {
        return icon;
    }

    /**
     * Set this Event's Icon.
     *
     * @param icon - The new Drawable icon for this Event.
     */
    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    /**
     * Set this Event's Title.
     *
     * @param title - The new Title for this Event.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns if this event is visible (public)
     *
     * @return true if public.
     */
    public boolean isVisible() {
        return isVisible;
    }

    /**
     * Changes the event's visibility.
     *
     * @param visible - Changes the visibility of the Event.
     */
    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    /**
     * Gets the Date object for this Event.
     *
     * @return the Java Date object for this Event.
     */
    public Date getEventDate() {
        return eventDate;
    }

    /**
     * Set the Date of this Event.
     *
     * @param eventDate - The new Date for this Event.
     */
    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    /**
     * Get the time of this Event.
     *
     * @return the Time of the Event.
     */
    public String getTime() {
        return time;
    }

    /**
     * Set the time for this Event.
     *
     * @param time - The new time for this Event.
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * Get the Price of the Event.
     *
     * @return the Ticket price of the Event.
     */
    public double getPrice() {
        return price;
    }

    /**
     * Set the price of the Event.
     *
     * @param price - The new ticket price for the Event.
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Get the participant capacity of this Event.
     *
     * @return the participant capacity for this Event.
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Set the participant capacity for this Event.
     *
     * @param capacity - The new participant capacity for this Event.
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    /**
     * Get the location of this Event.
     *
     * @return the location for this Event.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Set the location for this Event.
     *
     * @param location - The new location of this Event.
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Get the type of Event this is. (Enum?)
     *
     * @return the type of Event this is.
     */
    public String getEventType() {
        return eventType;
    }

    /**
     * Set the type of Event this is. (Enum?)
     *
     * @param eventType - The new type of Event this is.
     */
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    /**
     * Get the Host of this Event!
     *
     * @return the username of this Event's host.
     */
    public String getHost() {
        return host;
    }

    /**
     * Set a new host for the Event.
     *
     * @param host - The new Host of the Event.
     */
    public void setHost(String host) {
        this.host = host;
    }
}
