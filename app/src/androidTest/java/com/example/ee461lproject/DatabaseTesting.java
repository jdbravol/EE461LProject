package com.example.ee461lproject;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.runner.AndroidJUnit4;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Date;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static junit.framework.Assert.assertNotNull;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class DatabaseTesting {

    @Before
    public void clearBefore(){

        Instrumentation mInstrumentation = getInstrumentation();
        // We register our interest in the activity
        Instrumentation.ActivityMonitor monitor = mInstrumentation.addMonitor(SplashActivity.class.getName(), null, false);
        // We launch it
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClassName(mInstrumentation.getTargetContext(), SplashActivity.class.getName());
        mInstrumentation.startActivitySync(intent);

        Activity currentActivity = getInstrumentation().waitForMonitor(monitor);
        assertNotNull(currentActivity);

        // We register our interest in the next activity from the sequence in this use case
        mInstrumentation.removeMonitor(monitor);
        monitor = mInstrumentation.addMonitor(Login.class.getName(), null, false);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference eventsRef = database.getReference("TestEvents");
        eventsRef.removeValue();

        DatabaseReference usersRef = database.getReference("TestUsers");
        usersRef.removeValue();

        while(Database.allEvents().size()>0){}

    }

    @After
    public void clearAfter(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference eventsRef = database.getReference("TestEvents");
        eventsRef.removeValue();

        DatabaseReference usersRef = database.getReference("TestUsers");
        usersRef.removeValue();

        while(Database.allEvents().size()>0){}
    }
    /*
    * test to see if events can be added to DB
    *  and that it will be received by the app
     */
    @Test
    public void makeEventTest(){
        Event testEvent = new Event("TestEvent", "TestOrg", new Date(), "TestLocation", "TestDescription", true, "TestCategory");
        Database.makeEvent(testEvent);

        while(Database.allEvents().size()<1){}

        assert(testEvent.equals(Database.allEvents().get(0)));
    }

    @Test
    public void updateEventTest(){
        Event testEvent = new Event("TestEvent", "TestOrg", new Date(), "TestLocation", "TestDescription", true, "TestCategory");
        Database.makeEvent(testEvent);

        while(Database.allEvents().size()<1){}

        assert(testEvent.equals(Database.allEvents().get(0)));

        testEvent.setEventName("TestEventChange");
        Database.updateEvent(testEvent);

        //wait to giv time for update to occur
        synchronized (this){
            try {
                wait(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        assert(testEvent.equals(Database.allEvents().get(0)));
    }

    /*
    * test to see if events can be removed
     */
    @Test
    public void deleteEventTest(){
        Event testEvent = new Event("TestEvent", "TestOrg", new Date(), "TestLocation", "TestDescription", true, "TestCategory");
        Database.makeEvent(testEvent);

        while(Database.allEvents().size()<1){}
        Database.deleteEvent(testEvent);
        while(Database.allEvents().size()>0){}
        assert(testEvent.equals(Database.allEvents().size() == 0));
    }

    /*
    * test to see if we filter future events correctly
    *
     */
    @Test
    public void futureEventTest(){
        Date d = new Date(2019, 2,9);
        Date past = new Date(2015, 2, 9);
        Event testEvent = new Event("TestEvent", "TestOrg", d, "TestLocation", "TestDescription", true, "TestCategory");
        Event testEvent2 = new Event("TestEvent2", "TestOrg", d, "TestLocation", "TestDescription", true, "TestCategory");
        Event testEvent3 = new Event("TestEvent3", "TestOrg", d, "TestLocation", "TestDescription", true, "TestCategory");
        Event testEvent4 = new Event("TestEvent4", "TestOrg", d, "TestLocation", "TestDescription", true, "TestCategory");
        Event testEvent5 = new Event("TestEvent5", "TestOrg", d, "TestLocation", "TestDescription", true, "TestCategory");
        Event pastEvent = new Event("TestEvent5", "TestOrg", past, "TestLocation", "TestDescription", true, "TestCategory");
        Database.makeEvent(testEvent);
        Database.makeEvent(testEvent2);
        Database.makeEvent(testEvent3);
        Database.makeEvent(testEvent4);
        Database.makeEvent(testEvent5);
        Database.makeEvent(pastEvent);

        while(Database.allEvents().size()<5){}

        int count = 0;
        for(Event e : Database.futureEvents(Database.allEvents())){
            Date current = new Date();
            if(e.getDate().compareTo(current)>0){
                count++;
            }
        }

        assert(count==5);
    }

    /*
  * test to see if we filter future events correctly
  *
   */
    @Test
    public void pastEventTest(){
        Date d = new Date(2015, 2,9);
        Date future = new Date(2019, 2,9);
        Event testEvent = new Event("TestEvent", "TestOrg", d, "TestLocation", "TestDescription", true, "TestCategory");
        Event testEvent2 = new Event("TestEvent2", "TestOrg", d, "TestLocation", "TestDescription", true, "TestCategory");
        Event testEvent3 = new Event("TestEvent3", "TestOrg", d, "TestLocation", "TestDescription", true, "TestCategory");
        Event testEvent4 = new Event("TestEvent4", "TestOrg", d, "TestLocation", "TestDescription", true, "TestCategory");
        Event testEvent5 = new Event("TestEvent5", "TestOrg", d, "TestLocation", "TestDescription", true, "TestCategory");
        Event futureEvent = new Event("TestEvent5", "TestOrg", future, "TestLocation", "TestDescription", true, "TestCategory");
        Database.makeEvent(testEvent);
        Database.makeEvent(testEvent2);
        Database.makeEvent(testEvent3);
        Database.makeEvent(testEvent4);
        Database.makeEvent(testEvent5);
        Database.makeEvent(futureEvent);

        while(Database.allEvents().size()<5){}

        int count = 0;
        for(Event e : Database.pastEvents(Database.allEvents())){
            Date current = new Date();
            if(e.getDate().compareTo(current)<0){
                count++;
            }
        }

        assert(count == 5);

    }

    /*
    * test to see if org filtering works
     */
    @Test
    public void orgEventTest(){
        Date d = new Date();
        Event testEvent = new Event("TestEvent", "TestOrg", d, "TestLocation", "TestDescription", true, "TestCategory");
        Event testEvent2 = new Event("TestEvent2", "OtherOrg", d, "TestLocation", "TestDescription", true, "TestCategory");
        Database.makeEvent(testEvent);
        Database.makeEvent(testEvent2);

        while(Database.allEvents().size()<2){}

        assert(Database.eventsByOrg(Database.allEvents(), "TestOrg").size() == 1);
    }

    /*
   * test to see if finding events by day works
    */
    @Test
    public void dayEventTest(){
        Date d = new Date();
        Date other = new Date(2017, 2, 9);
        Event testEvent = new Event("TestEvent", "TestOrg", d, "TestLocation", "TestDescription", true, "TestCategory");
        Event testEvent2 = new Event("TestEvent2", "OtherOrg", other, "TestLocation", "TestDescription", true, "TestCategory");
        Database.makeEvent(testEvent);
        Database.makeEvent(testEvent2);

        while(Database.allEvents().size()<2){}

        assert(Database.dayEvents(Database.allEvents(), new Date()).size() == 1);
    }

    /*
    * test to see if the RSVP list is working correctly
    */
    @Test
    public void RSVPEventTest(){
        Date d = new Date();
        Event testEvent = new Event("TestEvent", "TestOrg", d, "TestLocation", "TestDescription", true, "TestCategory");
        Event testEvent2 = new Event("TestEvent2", "TestOrg", d, "TestLocation", "TestDescription", true, "TestCategory");
        testEvent.addToRSVP("USER");
        testEvent.addToRSVP("USER2");
        testEvent.addToRSVP("USER3");
        testEvent.addToRSVP("USER4");
        testEvent.addToRSVP("USER5");
        Database.makeEvent(testEvent);
        Database.makeEvent(testEvent2);

        while(Database.allEvents().size()<2){}

        ArrayList<Event> all = Database.allEvents();
        assert(Database.RSVPEvents(all, "USER").size() == 1);
        assert(Database.RSVPEvents(all, "USER2").size() == 1);
        assert(Database.RSVPEvents(all, "USER3").size() == 1);
        assert(Database.RSVPEvents(all, "USER4").size() == 1);
        assert(Database.RSVPEvents(all, "USER5").size() == 1);

    }

    /*
    * test to see if free food filtering works
    */
    @Test
    public void freeFoodEventTest(){
        Date d = new Date();
        Event testEvent = new Event("TestEvent", "TestOrg", d, "TestLocation", "TestDescription", true, "TestCategory");
        Event testEvent2 = new Event("TestEvent2", "OtherOrg", d, "TestLocation", "TestDescription", false, "TestCategory");
        Database.makeEvent(testEvent);
        Database.makeEvent(testEvent2);

        while(Database.allEvents().size()<2){}

        assert(Database.freeFoodEvents(Database.allEvents()).size() == 1);
    }
    /*
    * test to see if free food filtering works
    */
    @Test
    public void categoryEventTest(){
        Date d = new Date();
        Event testEvent = new Event("TestEvent", "TestOrg", d, "TestLocation", "TestDescription", true, "TestCategory");
        Event testEvent2 = new Event("TestEvent2", "OtherOrg", d, "TestLocation", "TestDescription", false, "Other");
        Database.makeEvent(testEvent);
        Database.makeEvent(testEvent2);

        while(Database.allEvents().size()<2){}

        assert(Database.categoryEvents(Database.allEvents(), "TestCategory").size() == 1);
    }

    /*
    * test making a lot of events
    * */
    @Test
    public void aLotOfEvents(){
        ArrayList<Event> events = new ArrayList<Event>();
        for(int i= 0; i<10; i++){
            Event e = new Event("TestEvent" + i, "TestOrg", new Date(), "TestLocation", "TestDescription", true, "TestCategory");
            events.add(e);
        }

        for(Event e : events){
            Database.makeEvent(e);
        }

        while(Database.allEvents().size()<10){}

        assert(Database.allEvents().size() == 10);
    }

    /*
 * test making of a user and tracking of their userType
 * */
    @Test
    public void testUser(){
        Database.makeUser("TESTUID1", "Student");
        Database.makeUser("TESTUID2", "Organization");
        Database.makeUser("TESTUID3", "Administrator");

        while(Database.numberUsers()<3){}

        assert(Database.getUserType("TESTUID1").equals("Student"));
        assert(Database.getUserType("TESTUID2").equals("Organization"));
        assert(Database.getUserType("TESTUID3").equals("Administrator"));
    }

}
