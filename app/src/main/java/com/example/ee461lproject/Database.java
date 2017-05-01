package com.example.ee461lproject;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Database{

    public static HashMap<String, Event> events = new HashMap<String,Event>();
    public static Object eventsWriteLock = new Object();
    public static Object usersWriteLock = new Object();
    public static HashMap<String, String> users = new HashMap<String, String>();

    /*
    * Takes input from firebase event listener
    * Takes the new or updated event and adds it to the event HashMap
     */
    public static void downloadEvent(Event event){
        Log.d("TEST", "Downloaded event" + event.getEventName());
        synchronized (eventsWriteLock) {
            events.put(event.getUniqueID(), event);
        }

    }

    /*
    * takes input from the firebase user listener
    * takes the new user and adds it to the user HashMap
     */
    public static void downloadUser(String id, String userType){
        Log.d("TESTUSER", "Downloaded user" + id);
        synchronized (usersWriteLock){
            users.put(id, userType);
        }
    }

    /*
   * gets the number of users in the hashmap
    */
    public static int numberUsers(){
        Log.d("TESTUSER", "Number of users");
        synchronized (usersWriteLock){
            return users.size();
        }
    }
    /*
    * when an org deletes an event, we remove it from the HashMap
     */
    public static void deleteEvent(Event event){
        synchronized (eventsWriteLock) {
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

        // TODO: We should delete this, although it's not necessary.
        // No user will have the event's unique ID, so it's not particularly dangerous to leave in.
        event.getRsvpList().put(event.getUniqueID(), true);

        newEventRef.setValue(event);
    }

    public static void makeUser(String id, String userType){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference("Users");
        DatabaseReference newUserRef = userRef.child(id);
        newUserRef.setValue(userType);
    }



    /*
    * returns the userType for a given user
    *
     */
    public static String getUserType(String id){
        synchronized (usersWriteLock){
            String userType = users.get(id);
            Log.d("getUserType",userType);
            return userType;
        }
    }

    /*
    * Returns an ArrayList of all events in the DB
    *
     */
    public static ArrayList<Event> allEvents(){

        synchronized (eventsWriteLock) {
            ArrayList<Event> eventList = new ArrayList<Event>();

            for(String s : events.keySet()){
                eventList.add(events.get(s));
            }

            return eventList;
        }

    }

    /*
    * Returns an ArrayList of all future events from the input ArrayList
    *
     */
    public static ArrayList<Event> futureEvents(ArrayList<Event> origEvents){

        synchronized (eventsWriteLock) {
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
        synchronized (eventsWriteLock) {
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
        synchronized (eventsWriteLock) {
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
        synchronized (eventsWriteLock) {
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
        synchronized (eventsWriteLock) {
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
        synchronized (eventsWriteLock) {
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
        synchronized (eventsWriteLock) {
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




