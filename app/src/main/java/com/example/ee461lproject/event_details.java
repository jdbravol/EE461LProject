package com.example.ee461lproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

        TextView title = (TextView) findViewById(R.id.eventNameOrg);
        title.setText(event.getEventName());
    }

}
