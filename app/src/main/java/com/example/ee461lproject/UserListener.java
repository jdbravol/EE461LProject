package com.example.ee461lproject;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public class UserListener implements ChildEventListener {

    private static final String TAG = "UserListener";

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName){
            Log.d(TAG,"onUserChildAdded:"+dataSnapshot.getKey());

            //there's a new user so we download it to the app
            String userType =  dataSnapshot.getValue(String.class);
            String userID = dataSnapshot.getKey();
            Database.downloadUser(userID, userType);
            }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot,String previousChildName){
            Log.d(TAG,"onUserChildChanged:"+dataSnapshot.getKey());

            //users are not going to change for now in our app
            }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot){
            Log.d(TAG,"onUserChildRemoved:"+dataSnapshot.getKey());
            }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot,String previousChildName){
            //this is not supposed to happen... if it does be worried :)
            Log.d(TAG,"onUserChildMoved:"+dataSnapshot.getKey());
            }

    @Override
    public void onCancelled(DatabaseError databaseError){
            //whelp
            Log.d(TAG,"onUserCancelled Error");
    }
}
