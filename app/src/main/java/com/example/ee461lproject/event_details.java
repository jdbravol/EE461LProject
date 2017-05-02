package com.example.ee461lproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class event_details extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        Intent i = getIntent();
        Event event = (Event) i.getParcelableExtra("EVENT");

        TextView title = (TextView) findViewById(R.id.eventNameStudent);
        title.setText(event.getEventName());
    }

}
