package com.example.ee461lproject;

import android.content.Entity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import org.parceler.Parcels;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class org_event_details extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_event_details);
        Intent i = getIntent();
        final Event event = (Event) Parcels.unwrap(i.getParcelableExtra("EVENT"));

        TextView title = (TextView) findViewById(R.id.eventTitleOrg);
        TextView nameField = (TextView) findViewById(R.id.nameFieldOrg);
        TextView dateField = (TextView) findViewById(R.id.dateFieldOrg);
        TextView locationField = (TextView) findViewById(R.id.locationFieldOrg);
        TextView descriptionText = (TextView) findViewById(R.id.descriptionFieldOrg);
        TextView categoryText = (TextView) findViewById(R.id.categoryFieldOrg);

        // TODO: Replace rsvpField w/ multi-line text box in layout
        // Currently, it's not displaying users on their own line
        TextView rsvpField = (TextView) findViewById(R.id.rsvpFieldOrg);

        CheckBox freeFood = (CheckBox) findViewById(R.id.freeFoodOrg);

        title.setText(event.getEventName());
        nameField.setText(event.getOrganizer());
        dateField.setText(event.getDateString());
        locationField.setText(event.getLocation());
        descriptionText.setText(event.getDescription());
        categoryText.setText(event.getCategory());
        if (event.hasFreeFood()){
            freeFood.setChecked(true);
        }
        else{
            freeFood.setChecked(false);
        }
        String RSVPlist = getRsvpList(event);

        rsvpField.setText(RSVPlist);
    }

    private String getRsvpList(Event event){
        HashMap<String, Boolean> rsvpMap = event.getRsvpList();
        String rsvpList = "";
        for (Map.Entry<String, Boolean> entry: rsvpMap.entrySet()) {
            rsvpList = rsvpList.concat(entry.getKey() + "\n");
        }
        return rsvpList;
    }
}
