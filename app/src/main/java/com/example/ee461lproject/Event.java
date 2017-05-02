package com.example.ee461lproject;


import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import org.parceler.Parcel;

/**
 * Created by ezuec on 4/22/2017.
 */

@Parcel
public class Event implements Comparable<Event>{

    String uniqueID;
    String eventName;
    String organizer;
    Date date;
    String location;
    String description;
    HashMap<String, Boolean> rsvpList;
    boolean freeFood;
    String category;
    int mData;

    public Event(){}

    public Event(String eventName, String organizer, Date date, String location, String description,
                 boolean freeFood, String category)
    {
        this.eventName = eventName;
        this.organizer = organizer;
        this.date = date;
        this.location = location;
        this.description = description;
        rsvpList = new HashMap<String, Boolean>();
        this.freeFood = freeFood;
        this.category = category;
    }

    // TODO: Delete this method in favor of the one below it?
    public ArrayList<String> getRSVPArrayList(){
        ArrayList<String> rsvp = new ArrayList<String>();

        for(String s : rsvpList.keySet()){
            if(rsvpList.get(s) == true){
                rsvp.add(s);
            }
        }
        return rsvp;
    }

    public HashMap<String, Boolean> getRsvpList() {
        return rsvpList;
    }

    public boolean isInRSVPList(String user){
        if(rsvpList.containsKey(user)){
            return true;
        }
        else{
            return false;
        }
    }

    // We need this additional getter so that the freeFood boolean shows up in Firebase
    public boolean isFreeFood() {
        return freeFood;
    }

    public boolean hasFreeFood() {
        return freeFood;
    }

    public void setFreeFood(boolean freeFood) {
        this.freeFood = freeFood;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public void addToRSVP(String user){
        rsvpList.put(user, true);
    }

    public void removeOfRSVP(String user){
        rsvpList.remove(user);
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }


    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object obj){
        Event other = (Event) obj;
        if(uniqueID!= other.uniqueID){ return false; }
        if(!category.equals(other.getCategory())){ return false;}
        if(!location.equals(other.getLocation())){ return false;}
        if(!description.equals(other.getDescription())){ return false;}
        if(!organizer.equals(other.getOrganizer())){ return false;}
        if(!rsvpList.equals(other.getRSVPArrayList())){ return false;}
        if(!eventName.equals(other.getEventName())){ return false;}
        if(freeFood!=other.hasFreeFood()) { return false;}
        if(!date.equals(other.getDate())) { return false;}

        return true;
    }

    @Override
    public int compareTo(@NonNull Event o) {
        if(date.compareTo(o.getDate())>0){
            return 1;
        }
        else{
            return -1;
        }
    }
}
