package com.example.ee461lproject;

import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;



/**
 * Created by Aaron on 4/17/17.
 */

public class Org_Tab2_CreatedFeed extends Fragment {

    private static final String TAG = "OrgTab2";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View orgCreateFeedView = inflater.inflate(R.layout.tab2_created_feed, container, false);
        ListView orgEventListView = (ListView) orgCreateFeedView.findViewById(R.id.org_eventListView);
        OrganizationOptions parent = (OrganizationOptions) getActivity();
        orgEventListView.setAdapter(parent.getEventFeedAdapter());

        orgEventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Event e = (Event) parent.getAdapter().getItem(position);
                Intent eventIntent = new Intent(getActivity(), event_details.class);
                eventIntent.putExtra("EVENT", (Parcelable) e);
            }
        });

        return orgCreateFeedView;
    }


}
