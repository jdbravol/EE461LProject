package com.example.ee461lproject;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Database{

    public static HashMap<String, Event> events = new HashMap<String,Event>();

    /*
    * Takes input from firebase listener
    * Takes the new or updated event and adds it to the HashMap
     */
    public static void downloadEvent(Event event){
        Log.d("TEST", "Downloaded event" + event.getEventName());
        events.put(event.getUniqueID(), event);
    }

    /*
    * when an org deletes an event
     */
    public static void deleteEvent(Event event){
        events.remove(event.getUniqueID());
    }

    /*
    * Updates the server side version of an Event
    * Meant for when organizers change the info of an event
     */
    public static void updateEvent(Event event){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference eventsRef = database.getReference("Events");

        //using our unique key, update the value of the db event
        DatabaseReference eventRef = eventsRef.child(event.getUniqueID());
        eventRef.setValue(event);
    }

    /*
    * Makes a new event in the Firebase DB
    * Used by organizers making new events to be displayed
     */
    public static void makeEvent(Event event){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference eventsRef = database.getReference("Events");

        //push a new reference and set the value to the new event
        DatabaseReference newEventRef = eventsRef.push();
        event.setUniqueID(newEventRef.getKey());
        newEventRef.setValue(event);
    }

    /*
    * Returns an ArrayList of all events in the DB
    *
     */
    public static ArrayList<Event> allEvents(){
        ArrayList<Event> eventList = new ArrayList<Event>();

        for(String s : events.keySet()){
            eventList.add(events.get(s));
        }

        return eventList;
    }

    /*
    * Returns an ArrayList of all future events in the DB
    *
     */
    public static ArrayList<Event> allFutureEvents(){
        return null;
    }

    /*
    * Returns an ArrayList of all past events in the DB
    *
     */
    public static ArrayList<Event> allPastEvents(){
        return null;
    }

    /*
    * Returns an ArrayList of all future events by a specific organizer
    *
     */
    public static ArrayList<Event> allFutureEventsByOrg(String org){
        return null;
    }

    /*
    * Returns an ArrayList of all past events by a specific organizer
    *
     */
    public static ArrayList<Event> allPastEventsByOrg(String org){
        return null;
    }

    /*
    * Returns an ArrayList of all event by a specific organizer
    *
     */
    public static ArrayList<Event> allEventsByOrg(String org){
        return null;
    }

    /*
    * Returns an ArrayList of all events a specific User is RSVPd to
    *
     */
    public static ArrayList<Event> allRSVPEvents(String user){
        return null;
    }

    /*
    * Returns an ArrayList of all past events a specific User is RSVPd to
    *
     */
    public static ArrayList<Event> allPastRSVPEvents(String user){
        return null;
    }

    /*
    * Returns an ArrayList of all future events a specific User is RSVPd to
    *
     */
    public static ArrayList<Event> allFutureRSVPEvents(String user){
        return null;
    }
}




