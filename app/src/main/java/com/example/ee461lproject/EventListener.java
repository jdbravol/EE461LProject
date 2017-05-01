package com.example.ee461lproject;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import java.lang.ClassLoader;

public class EventListener implements ChildEventListener {

    private static final String TAG = "EventListener";

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName){
        Log.d(TAG,"onChildAdded:"+dataSnapshot.getKey());

        //there's a new event so we make it on our offline db
        Event event =  dataSnapshot.getValue(Event.class);
        Database.downloadEvent(event);
        if (Database.writeToOrg) {
            OrganizationOptions.updateUnderlyingEvents(event.getOrganizer());
        }
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot,String previousChildName){
        Log.d(TAG,"onChildChanged:"+dataSnapshot.getKey());

        //an event was updated, so we update it
        Event event =  dataSnapshot.getValue(Event.class);
        Database.downloadEvent(event);
        if (Database.writeToOrg) {
            OrganizationOptions.updateUnderlyingEvents(event.getOrganizer());
        }
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot){
        Log.d(TAG,"onChildRemoved:"+dataSnapshot.getKey());

        //an event was removed, so eliminate it
        Event event =  dataSnapshot.getValue(Event.class);
        Database.deleteEvent(event);
        if (Database.writeToOrg) {
            OrganizationOptions.updateUnderlyingEvents(event.getOrganizer());
        }
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot,String previousChildName){
        //this is not supposed to happen... if it does be worried :)
        Log.d(TAG,"onChildMoved:"+dataSnapshot.getKey());
    }

    @Override
    public void onCancelled(DatabaseError databaseError){
        //whelp
        Log.d(TAG,"onCancelled Error");
    }
}
