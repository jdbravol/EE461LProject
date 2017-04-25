package com.example.ee461lproject;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class Database{

    public static HashMap<String, Event> events = new HashMap<String,Event>();
    public static Object writeLock = new Object();
    /*
    * Takes input from firebase listener
    * Takes the new or updated event and adds it to the HashMap
     */
    public static void downloadEvent(Event event){
        Log.d("TEST", "Downloaded event" + event.getEventName());
        synchronized (writeLock) {
            events.put(event.getUniqueID(), event);
        }

    }

    /*
    * when an org deletes an event
     */
    public static void deleteEvent(Event event){
        synchronized (writeLock) {
            events.remove(event.getUniqueID());
        }

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

        synchronized (writeLock) {
            ArrayList<Event> eventList = new ArrayList<Event>();

            for(String s : events.keySet()){
                eventList.add(events.get(s));
            }

            return eventList;
        }

    }

    /*
    * Returns an ArrayList of all future events from the input ArrayLisy
    *
     */
    public static ArrayList<Event> futureEvents(ArrayList<Event> origEvents){

        synchronized (writeLock) {
            Date currentDate = new Date();
            ArrayList<Event> futureEvents = new ArrayList<Event>();

            for(Event e : origEvents){
                if(e.getDate().compareTo(currentDate)>= 0){
                    futureEvents.add(e);
                }
            }
            return futureEvents;
        }

    }

    /*
    * Returns an ArrayList of all past events in the input ArrayList
    *
     */
    public static ArrayList<Event> pastEvents(ArrayList<Event> origEvents){
        synchronized (writeLock) {
            Date currentDate = new Date();
            ArrayList<Event> pastEvents = new ArrayList<Event>();

            for(Event e : origEvents){
                if(e.getDate().compareTo(currentDate)< 0){
                    pastEvents.add(e);
                }
            }
            return pastEvents;
        }

    }

    /*
    * Returns an ArrayList of all events from a specific organizer from the input AL
    *
     */
    public static ArrayList<Event> eventsByOrg(ArrayList<Event> origEvents, String org){
        synchronized (writeLock) {
            Date currentDate = new Date();
            ArrayList<Event> eventsByOrg = new ArrayList<Event>();

            for(Event e : origEvents){
                if(e.getDate().compareTo(currentDate)>= 0 && org.equals(e.getOrganizer())){
                    eventsByOrg.add(e);
                }
            }
            return eventsByOrg;
        }
    }

    /*
    * Returns an ArrayList of all events a specific User is RSVPd to
    *
     */
    public static ArrayList<Event> RSVPEvents(ArrayList<Event> origEvents, String user){
        synchronized (writeLock) {
            ArrayList<Event> rsvpEvents = new ArrayList<Event>();

            for(Event e : origEvents){
                if(e.isInRSVPList(user)){
                    rsvpEvents.add(e);
                }
            }

            return rsvpEvents;
        }
    }

    /*
    * Returns an ArrayList of all events that match a Date
     */
    public static ArrayList<Event> dayEvents(ArrayList<Event> origEvents, Date date){
        synchronized (writeLock) {
            ArrayList<Event> dayEvents = new ArrayList<Event>();

            for(Event e : origEvents){
                Date eventDate = e.getDate();
                if(eventDate.getDay()==date.getDay() &&
                        eventDate.getMonth()==date.getMonth() &&
                        eventDate.getYear() == date.getYear()){
                    dayEvents.add(e);
                }
            }

            return dayEvents;
        }
    }

    /*
    * Returns an ArrayList for events that match a category
     */
    public static ArrayList<Event> categoryEvents(ArrayList<Event> origEvents, String category){
        synchronized (writeLock) {
            ArrayList<Event> catEvents = new ArrayList<Event>();

            for(Event e : origEvents){
                if(e.getCategory().equals(category)){
                    catEvents.add(e);
                }
            }

            return catEvents;
        }
    }

    /*
    * Returns an ArrayList for events that have free food
     */

    public static ArrayList<Event> freeFoodEvents(ArrayList<Event> origEvents){
        synchronized (writeLock) {
            ArrayList<Event> freeFoodEvents = new ArrayList<Event>();

            for(Event e : origEvents){
                if(e.hasFreeFood()){
                    freeFoodEvents.add(e);
                }
            }

            return freeFoodEvents;
        }
    }

}




