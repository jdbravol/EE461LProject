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
    private static final String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul",
            "Aug", "Sep", "Oct", "Nov", "Dec"};
    private String month;
    private String year;

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
        dateText.setText(getDateString(event));
        locationText.setText(event.getLocation());
        descriptionText.setText(event.getDescription());
        title.setText(event.getEventName());
    }

    private void setMonthAndYear(String origMonth, String origYear) {
        int monthIndex;
        for (monthIndex = 0; monthIndex < 12; monthIndex++) {
            if (origMonth.equals(months[monthIndex])) {
                break;
            }
        }

        int adjustedYear = 0;
        monthIndex = monthIndex - 1;
        if (monthIndex < 0) {
            monthIndex += 12;
            adjustedYear = Integer.parseInt(origYear) - 1901;
        }
        else {
            adjustedYear = Integer.parseInt(origYear) - 1900;
        }
        year = String.valueOf(adjustedYear);
        month = months[monthIndex] + ".";
    }
     private String getDateString(Event e){
         String[] dateFields = e.getDate().toString().split(" ");
         String time = "";
         String dayOfMonth = "";
         String date = "";
         month = "";
         year = "";
         setMonthAndYear(dateFields[1], dateFields[5]);
         dayOfMonth = dateFields[2].trim();
         time = dateFields[3].trim();
         date = month + " " + dayOfMonth + ", " + year + " at " + time;
         Log.d(TAG, "the date string: " + date);
         return date;
     }

}
