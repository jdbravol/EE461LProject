package com.example.ee461lproject;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

import static java.lang.String.*;

/**
 * Created by Aaron on 4/17/17.
 */

// TODO: Find better way to handle scrolling when keyboard is up on screen...
public class Org_Tab1_CreateEvent extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        View rootView =  inflater.inflate(R.layout.tab1_create_event, container, false);

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

                Calendar mcurrentDate=Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth=mcurrentDate.get(Calendar.MONTH);
                int mDay=mcurrentDate.get(Calendar.DAY_OF_MONTH);

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
                        if(String.valueOf(hour).length()>1){
                            a = hour + ":" + minute;
                        }
                        else{
                            a = hour + ":0" + minute;
                        }

                        editTextTime.setText(""+a);
                    }
                };

                Calendar mcurrentDate=Calendar.getInstance();
                int mHour = mcurrentDate.get(Calendar.HOUR);
                int mMinute = mcurrentDate.get(Calendar.MINUTE);

                TimePickerDialog dialog = new TimePickerDialog(getActivity(), tpd, mHour, mMinute, false);
                dialog.show();

            }
        });


        return rootView;
    }
}
