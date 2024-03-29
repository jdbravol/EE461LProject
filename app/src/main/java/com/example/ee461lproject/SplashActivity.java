package com.example.ee461lproject;

/**
 * Created by juanbravo on 4/18/17.
 */

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SplashActivity extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 2000;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {

        setContentView(R.layout.activity_splash);
        super.onCreate(icicle);
        if(FirebaseAuth.getInstance().getCurrentUser() != null){ FirebaseAuth.getInstance().signOut(); }

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                //IMPORTANT: registers a listener to update the events
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference eventsRef = database.getReference("Events");
                eventsRef.addChildEventListener(new EventListener());

                //IMPORTANT: registers a listener to update the users -- (if slow, will be moved)
                DatabaseReference usersRef = database.getReference("Users");
                usersRef.addChildEventListener(new UserListener());

                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(SplashActivity.this, Login.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
