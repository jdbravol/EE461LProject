package com.example.ee461lproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import org.parceler.Parcels;

/**
 * Created by juanbravo on 4/18/17.
 */

public class Student_Tab2_MainEvents extends Fragment {

    private String TAG = "Student_Tab2";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.tab2_main_events, container, false);
        ListView mainEventsFeed = (ListView) rootView.findViewById(R.id.student_eventListView);

        final Button backToMainFeed = (Button) rootView.findViewById(R.id.backToMainFeed);

        StudentOptions parent = (StudentOptions) getActivity();
        mainEventsFeed.setAdapter(parent.getMainFeedAdapter());

        mainEventsFeed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Event e = (Event) parent.getAdapter().getItem(position);
                Log.d(TAG, "Event Name is: " + e.getEventName());
                Intent eventIntent = new Intent(getActivity(), event_details.class);
                eventIntent.putExtra("EVENT", Parcels.wrap(e));
                getActivity().finish();
                startActivity(eventIntent);
            }
        });

        backToMainFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StudentOptions.searchMode.set(false);
                backToMainFeed.setVisibility(View.GONE);
                StudentOptions.updateUnderlyingEvents();
            }
        });

        return rootView;
    }



}
