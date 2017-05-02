package com.example.ee461lproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.parceler.Parcels;

public class org_event_details extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_event_details);
        Intent i = getIntent();
        Event event = (Event) Parcels.unwrap(i.getParcelableExtra("EVENT"));
    }
}
