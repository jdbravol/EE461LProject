package com.example.ee461lproject;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by juanbravo on 4/18/17.
 */

public class Student_Tab2_MainEvents extends Fragment {



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2_main_events, container, false);



        //linking
        ListView mainEventsFeed = (ListView) rootView.findViewById(R.id.student_eventListView);
        StudentOptions parent = (StudentOptions) getActivity();
        mainEventsFeed.setAdapter(parent.getAllEventFeedAdapter());


        mainEventsFeed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO: open description of event
            }
        });
        return rootView;
    }



}
