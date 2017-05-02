package com.example.ee461lproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import org.parceler.Parcels;

public class event_details extends AppCompatActivity {

    private String TAG = "event_details";
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        Intent i = getIntent();
        final Event event = (Event) Parcels.unwrap(i.getParcelableExtra("EVENT"));

        TextView title = (TextView) findViewById(R.id.eventNameStudent);
        TextView orgText = (TextView) findViewById(R.id.orgTextStudent);
        TextView dateText = (TextView) findViewById(R.id.dateTextStudent);
        TextView locationText = (TextView) findViewById(R.id.locationTextStudent);
        TextView descriptionText = (TextView) findViewById(R.id.descriptionTextStudent);
        final Button rsvpButton = (Button) findViewById(R.id.rsvpButtonStudent);

        orgText.setText(event.getOrganizer());
        dateText.setText(event.getDateString());
        locationText.setText(event.getLocation());
        descriptionText.setText(event.getDescription());
        title.setText(event.getEventName());

        if(event.isInRSVPList(mAuth.getCurrentUser().getDisplayName())){
            rsvpButton.setText("UNRSVP");
        }
        rsvpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(event.isInRSVPList(mAuth.getCurrentUser().getDisplayName())){
                    event.removeOfRSVP(mAuth.getCurrentUser().getDisplayName());
                    Database.updateEvent(event);
                    rsvpButton.setText("RSVP");
                }

                else{
                    event.addToRSVP(mAuth.getCurrentUser().getDisplayName());
                    Database.updateEvent(event);
                    rsvpButton.setText("UNRSVP");
                }
                StudentOptions.updateUnderlyingEvents();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // TODO: Find a way to go back to main feed rather than search page
            Intent userOptionsIntent = new Intent(this, StudentOptions.class);
            startActivity(userOptionsIntent);
            this.finish();
        }

        return super.onKeyDown(keyCode, event);
    }


}
