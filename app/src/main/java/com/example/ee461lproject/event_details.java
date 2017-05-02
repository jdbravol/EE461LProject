package com.example.ee461lproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import org.parceler.Parcels;

public class event_details extends AppCompatActivity {

    private String TAG = "event_details";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        Intent i = getIntent();
        Event event = (Event) Parcels.unwrap(i.getParcelableExtra("EVENT"));

        TextView title = (TextView) findViewById(R.id.eventNameStudent);
        TextView orgText = (TextView) findViewById(R.id.orgTextStudent);
        TextView dateText = (TextView) findViewById(R.id.dateTextStudent);
        TextView locationText = (TextView) findViewById(R.id.locationTextStudent);
        TextView descriptionText = (TextView) findViewById(R.id.descriptionTextStudent);


        orgText.setText(event.getOrganizer());
        dateText.setText(event.getDateString());
        locationText.setText(event.getLocation());
        descriptionText.setText(event.getDescription());
        title.setText(event.getEventName());
    }


}
