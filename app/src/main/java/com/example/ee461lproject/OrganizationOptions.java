package com.example.ee461lproject;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Collections;

public class OrganizationOptions extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private static final String TAG = "OrgOptions";
    public static ArrayList<Event> underlyingOrgEvents;
    public static Context context;
    public static EventFeedAdapter eventFeedAdapter;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization_options);
        underlyingOrgEvents = Database.allEvents();
        Collections.sort(underlyingOrgEvents);

        OrganizationOptions.context = getApplicationContext();
        FirebaseUser user = mAuth.getInstance().getCurrentUser();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the two
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        ArrayList<Event> orgEvents = Database.eventsByOrg(underlyingOrgEvents, user.getDisplayName());
        eventFeedAdapter = new EventFeedAdapter(context, orgEvents);
    }


    public static void updateUnderlyingEvents(String orgName) {
        Log.d(TAG, "entering updateUnderlyingEvents()");

        ArrayList<Event> allEvents = Database.allEvents();
        underlyingOrgEvents = Database.eventsByOrg(allEvents, orgName);
        Collections.sort(underlyingOrgEvents);
        eventFeedAdapter.clear();
        eventFeedAdapter.addAll(underlyingOrgEvents);
        eventFeedAdapter.notifyDataSetChanged();

        Log.d(TAG, "size of underlyingOrgEvents: " + underlyingOrgEvents.size());
    }


    public EventFeedAdapter getEventFeedAdapter() {
        return eventFeedAdapter;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_organization_name, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(OrganizationOptions.this, Settings.class);
            OrganizationOptions.this.startActivity(settingsIntent);
            OrganizationOptions.this.finish();
        }

        else if(id == R.id.action_logout){
            Log.d(TAG, "Clicked logout");
            mAuth.signOut();
            Intent splashIntent = new Intent(OrganizationOptions.this, SplashActivity.class);
            OrganizationOptions.this.startActivity(splashIntent);
            OrganizationOptions.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    // DELETED PlaceholderFragment

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    Org_Tab1_CreateEvent tab1 = new Org_Tab1_CreateEvent();
                    return tab1;
                case 1:
                    Org_Tab2_CreatedFeed tab2 = new Org_Tab2_CreatedFeed();
                    return tab2;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "CREATE";
                case 1:
                    return "FEED";
            }
            return null;
        }
    }
}
