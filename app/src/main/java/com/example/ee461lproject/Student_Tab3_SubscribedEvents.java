package com.example.ee461lproject;

import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

/**
 * Created by juanbravo on 4/18/17.
 */

public class Student_Tab3_SubscribedEvents extends Fragment {

    public EventFeedAdapter subscribedEventFeed;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab3_subscribed_events, container, false);

        ListView subscribedListView = (ListView) rootView.findViewById(R.id.subscribedEventListView);

        return rootView;
    }
}