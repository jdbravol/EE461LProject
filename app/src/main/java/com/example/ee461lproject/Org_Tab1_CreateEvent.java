package com.example.ee461lproject;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static java.lang.String.*;

/**
 * Created by Aaron on 4/17/17.
 */

public class Org_Tab1_CreateEvent extends Fragment {

    private EditText eventName;
    private EditText eventDate;
    private EditText eventTime;
    private EditText eventLocation;
    private EditText eventDescription;
    private EditText eventCategory;
    private CheckBox freeFoodCheckBox;
    private Button createEvent;
    private ArrayList<String> textFields = new ArrayList<>();

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private static final String TAG = "OrgTab1";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.tab1_create_event, container, false);

        //this beautiful line of code - love it
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Log.d(TAG, "Current User: " + user.getDisplayName());

        final EditText editText = (EditText) rootView.findViewById(R.id.org_EventDate);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog.OnDateSetListener dpd = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        int s=monthOfYear+1;
                        String a = dayOfMonth+"/"+s+"/"+year;
                        editText.setText(""+a);
                    }
                };

                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getActivity(), dpd, mYear, mMonth, mDay);
                dialog.show();

            }
        });

        final EditText editTextTime = (EditText) rootView.findViewById(R.id.org_EventTime);
        editTextTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimePickerDialog.OnTimeSetListener tpd = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hour, int minute) {
                        String a;

                        if(minute >= 10){
                            a = hour + ":" + minute;
                        }
                        else{
                            a = hour + ":0" + minute;
                        }

                        editTextTime.setText(""+a);
                    }
                };

                Calendar mcurrentDate = Calendar.getInstance();
                int mHour = mcurrentDate.get(Calendar.HOUR);
                int mMinute = mcurrentDate.get(Calendar.MINUTE);

                TimePickerDialog dialog = new TimePickerDialog(getActivity(), tpd, mHour, mMinute, false);
                dialog.show();

            }
        });

        setWidgets(rootView);

        createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textFields.add(eventName.getText().toString().trim());
                textFields.add(eventDate.getText().toString().trim());
                textFields.add(eventTime.getText().toString().trim());
                textFields.add(eventLocation.getText().toString().trim());
                textFields.add(eventDescription.getText().toString().trim());
                textFields.add(eventCategory.getText().toString().trim());

                if (!textFieldsEmpty()) {
                    Event e = constructEvent();
                    Database.makeEvent(e);
                }
                else {
                    Toast.makeText(getActivity(),
                            "Fill out the form completely.",
                            Toast.LENGTH_SHORT).show();
                }

                textFields.clear();
                clearAllWidgets();
            }
        });

        return rootView;
    }

    private void setWidgets(View rootView) {
        eventName = (EditText) rootView.findViewById(R.id.org_EventName);
        eventDate = (EditText) rootView.findViewById(R.id.org_EventDate);
        eventTime = (EditText) rootView.findViewById(R.id.org_EventTime);
        eventLocation = (EditText) rootView.findViewById(R.id.org_EventLocation);
        eventDescription = (EditText) rootView.findViewById(R.id.org_EventDescription);
        eventCategory = (EditText) rootView.findViewById(R.id.org_EventCategory);
        freeFoodCheckBox = (CheckBox) rootView.findViewById(R.id.free_food_checkbox);
        createEvent = (Button) rootView.findViewById(R.id.org_CreateEvent);
    }

    private Event constructEvent() {
        String eventNameString = eventName.getText().toString().trim();
        String eventDateString = eventDate.getText().toString().trim();
        String eventTimeString = eventTime.getText().toString().trim();
        String eventLocationString = eventLocation.getText().toString().trim();
        String eventDescriptionString = eventDescription.getText().toString().trim();
        String eventCategoryString = eventCategory.getText().toString().trim();

        user = FirebaseAuth.getInstance().getCurrentUser();

        String orgName = user.getDisplayName();
        Date date = constructDate(eventDateString, eventTimeString);
        boolean freeFood = freeFoodCheckBox.isChecked();

        Event dataBaseEvent = new Event(eventNameString, orgName, date, eventLocationString,
                                        eventDescriptionString, freeFood, eventCategoryString);

        return dataBaseEvent;
    }

    private Date constructDate(String DD_MM_YYYY, String HH_MM) {
        String[] dayMonthYear = DD_MM_YYYY.split("/");
        String[] hourMinute = HH_MM.split(":");

        int day = Integer.parseInt(dayMonthYear[0]);
        int month = Integer.parseInt(dayMonthYear[1]);
        int year = Integer.parseInt(dayMonthYear[2]);

        int hour = Integer.parseInt(hourMinute[0]);
        int minute = Integer.parseInt(hourMinute[1]);

        Date date = new Date(year, month, day, hour, minute);
        Log.d(TAG, "The date is: " + date.toString());
        return date;
    }

    private void clearAllWidgets() {
        eventName.setText("");
        eventDate.setText("");
        eventTime.setText("");
        eventLocation.setText("");
        eventDescription.setText("");
        eventCategory.setText("");
        freeFoodCheckBox.setChecked(false);
    }

    private boolean textFieldsEmpty() {
        for (String field : textFields) {
            if (TextUtils.isEmpty(field)) {
                return true;
            }
        }

        return false;
    }
}
