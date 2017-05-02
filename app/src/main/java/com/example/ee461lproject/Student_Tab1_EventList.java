package com.example.ee461lproject;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Michael Glasser on 5/1/2017.
 */

public class Student_Tab1_EventList extends Fragment {

    public void Student_Tab1_EventList(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        //set view
        View rootView =  inflater.inflate(R.layout.tab1_searched_events_list, container, false);
        ListView mainEventsFeed = (ListView) rootView.findViewById(R.id.Student_searchedEventsListView);
        StudentOptions parent = (StudentOptions) getActivity();
        mainEventsFeed.setAdapter(parent.getAllEventFeedAdapter());

        mainEventsFeed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Event e = (Event) parent.getAdapter().getItem(position);
                Intent eventIntent = new Intent(getActivity(), event_details.class);
                eventIntent.putExtra("EVENT", (Parcelable) e);
            }
        });

        // TODO make a function that goes through list of all events and checks it against
        //      org name


        return rootView;
    }


}
