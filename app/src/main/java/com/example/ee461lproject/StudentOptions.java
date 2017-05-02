package com.example.ee461lproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

public class StudentOptions extends AppCompatActivity {
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */

    private StudentOptions.SectionsPagerAdapter mSectionsPagerAdapter;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private final static String TAG = "StudentOptionsActivity";
    public static ArrayList<Event> allEventList = Database.allEvents();
    public static Context context;
    public static EventFeedAdapter mainFeedAdapter;
    public static EventFeedAdapter subscribedEventFeedAdapter;
    public static String user;
    public static AtomicBoolean searchMode = new AtomicBoolean(false);
    public static final Object mainFeedAdapterLock = new Object();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(TAG, "StudentOptions::onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_feed);
        StudentOptions.context = getApplicationContext();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        user = mAuth.getCurrentUser().getDisplayName();
        Log.d(TAG, "User is " + user);
        // Create the adapter that will return a fragment for each of the two
        // primary sections of the activity.
        mSectionsPagerAdapter = new StudentOptions.SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        // Fetch the list of events again as static initialization only runs once.
        // Otherwise, in the case that we make an event in an org account, log out, then log into a
        // student account, the new event wouldn't show up in the student's feed.
        allEventList = Database.allEvents();

        searchMode = new AtomicBoolean(false);
        Collections.sort(allEventList);
        mainFeedAdapter = new EventFeedAdapter(context, allEventList);
        ArrayList<Event> subscribedEvents = Database.RSVPEvents(allEventList, mAuth.getCurrentUser().getDisplayName());
        Log.d(TAG, "size of: " + subscribedEvents.size());
        subscribedEventFeedAdapter = new EventFeedAdapter(context, subscribedEvents);
    }

    public EventFeedAdapter getMainFeedAdapter(){
        return mainFeedAdapter;
    }

    public EventFeedAdapter getSubscribedFeedAdapter(){
        return subscribedEventFeedAdapter;
    }

    public static void updateUnderlyingEvents() {
        Log.d(TAG, "entering updateUnderlyingEvents()");

        // TODO: Test searchMode to make sure feed doesn't change when a new event is added
        synchronized (mainFeedAdapterLock) {
            allEventList = Database.allEvents();
            Collections.sort(allEventList);

            if (!searchMode.get()) {
                mainFeedAdapter.clear();
                mainFeedAdapter.addAll(allEventList);
                mainFeedAdapter.notifyDataSetChanged();
            }

            subscribedEventFeedAdapter.clear();
            subscribedEventFeedAdapter.addAll(Database.RSVPEvents(allEventList, user));
            subscribedEventFeedAdapter.notifyDataSetChanged();
        }

        Log.d(TAG, "size of underlyingOrgEvents: " + allEventList.size());
    }

    public void filterAdapterByOrg(String orgName) {

        synchronized (mainFeedAdapterLock) {
            allEventList = Database.allEvents();
            Collections.sort(allEventList);
            mainFeedAdapter.clear();
            mainFeedAdapter.addAll(Database.eventsByOrg(allEventList, orgName));
            mainFeedAdapter.notifyDataSetChanged();
        }

    }

    public void filterAdapterByEverythingElse(String cat, String date, boolean freeFood){
        synchronized (mainFeedAdapterLock){
            allEventList = Database.allEvents();
            if(!cat.equals("") ){
                allEventList = Database.categoryEvents(allEventList, cat.trim());
            }
            if(!date.equals("")){
                date = date.trim();
                Log.d(TAG, "The date string is: " + date);
                String[] dateFields = date.split("/");
                int day = Integer.parseInt(dateFields[0]);
                int month = Integer.parseInt(dateFields[1]);
                int year = Integer.parseInt(dateFields[2]);
                Date d = new Date(year, month, day);
                allEventList = Database.dayEvents(allEventList, d);
            }
            if(freeFood){
                allEventList = Database.freeFoodEvents(allEventList);
            }

            Collections.sort(allEventList);
            mainFeedAdapter.clear();
            mainFeedAdapter.addAll(allEventList);
            mainFeedAdapter.notifyDataSetChanged();

        }
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
        Log.d(TAG, "onOptionsItemSelected");
        long id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Log.d(TAG, "Clicked action_settings");
            Intent settingsIntent = new Intent(StudentOptions.this, Settings.class);
            StudentOptions.this.startActivity(settingsIntent);
            StudentOptions.this.finish();
        }

        else if(id == R.id.action_logout){
            Log.d(TAG, "Clicked logout");
            mAuth.signOut();
            Intent splashIntent = new Intent(StudentOptions.this, SplashActivity.class);
            StudentOptions.this.startActivity(splashIntent);
            StudentOptions.this.finish();
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
                    Student_Tab1_SearchEvents tab1 = new Student_Tab1_SearchEvents();
                    return tab1;
                case 1:
                    Student_Tab2_MainEvents tab2 = new Student_Tab2_MainEvents();
                    return tab2;
                case 2:
                    Student_Tab3_SubscribedEvents tab3 = new Student_Tab3_SubscribedEvents();
                    return tab3;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SEARCH";
                case 1:
                    return "MAIN";
                case 2:
                    return "SUBSCRIBED";
            }
            return null;
        }
    }
}
