package com.example.ee461lproject;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by juanbravo on 4/18/17.
 */

public class Student_Tab2_MainEvents extends Fragment {
    private static final String TAG = "StudentTab2";
    public static ArrayList<Event> allEventList = Database.allEvents();
    public static Context context;
    public static EventFeedAdapter eventFeedAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2_main_events, container, false);

        eventFeedAdapter = new EventFeedAdapter(context, allEventList);
        //linking
        ListView mainEventsFeed = (ListView) rootView.findViewById(R.id.student_eventListView);
        OrganizationOptions parent = (OrganizationOptions) getActivity();
        mainEventsFeed.setAdapter(parent.getEventFeedAdapter());

        return rootView;
    }

    public static void updateUnderlyingEvents() {
        Log.d(TAG, "entering updateUnderlyingEvents()");

        allEventList = Database.allEvents();
        eventFeedAdapter.clear();
        eventFeedAdapter.addAll(allEventList);
        eventFeedAdapter.notifyDataSetChanged();

        Log.d(TAG, "size of underlyingOrgEvents: " + allEventList.size());
    }

    public EventFeedAdapter getAllEventFeedAdapter() {
        return eventFeedAdapter;
    }
}
