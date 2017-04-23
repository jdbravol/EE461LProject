package com.example.ee461lproject;

/**
 * Created by juanbravo on 4/18/17.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class SplashActivity extends Activity{
    private final int SPLASH_DISPLAY_LENGTH = 5000;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {

        setContentView(R.layout.activity_splash);
        super.onCreate(icicle);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference eventsRef = database.getReference("Events");

        eventsRef.addChildEventListener(new EventListener());

        Event event = new Event("TestEvent", "TestOrg", new Date(), "Test Location", "Test descript");
        Database.makeEvent(event);

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(SplashActivity.this,StudentOptions.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
