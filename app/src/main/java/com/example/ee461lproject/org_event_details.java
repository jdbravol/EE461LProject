package com.example.ee461lproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import org.parceler.Parcels;

import java.util.HashMap;
import java.util.Map;

public class org_event_details extends AppCompatActivity {

    private static final String TAG = "org_event_details";

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
        Button deleteButton = (Button) findViewById(R.id.deleteButton);

        // TODO: Replace rsvpField w/ multi-line text box in layout
        // Currently, it's not displaying users on their own line
        TextView rsvpField = (TextView) findViewById(R.id.rsvpFieldOrg);
        CheckBox freeFood = (CheckBox) findViewById(R.id.freeFoodOrg);

        title.setText(event.getEventName());
        nameField.setText(event.getOrganizer());
        dateField.setText(event.constructDateString());
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

        //delete the event
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Database.deleteEvent(event);
                OrganizationOptions.updateUnderlyingEvents(event.getOrganizer());
                Intent orgOptions = new Intent(org_event_details.this, OrganizationOptions.class);
                startActivity(orgOptions);
                org_event_details.this.finish();
            }
        });
    }

    private String getRsvpList(Event event){
        HashMap<String, Boolean> rsvpMap = event.getRsvpList();
        Log.d(TAG, "org_event_details::getRsvpList -- rsvpMap ref value: " + rsvpMap);

        // We need a null-check as an empty rsvp list on Firebase returns a null reference.
        // (As opposed to an initialized, but non-null reference.)
        if (rsvpMap == null) {
            return "No one has RSVP'd.";
        }

        String rsvpList = "";
        for (Map.Entry<String, Boolean> entry: rsvpMap.entrySet()) {
            rsvpList = rsvpList.concat(entry.getKey() + "\n");
        }
        return rsvpList;
    }
}
