package com.example.ee461lproject;

import android.app.DatePickerDialog;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by juanbravo on 4/17/17.
 */

public class Student_Tab1_SearchEvents extends Fragment {



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        //set view
        View rootView =  inflater.inflate(R.layout.tab1_search_events, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        //Link objects in fragment
        final EditText dateField = (EditText) rootView.findViewById(R.id.dateField);
        final EditText orgName = (EditText) rootView.findViewById(R.id.orgNameField);
        final EditText category = (EditText) rootView.findViewById(R.id.categoryField);
        final Button goButtonOrgName = (Button) rootView.findViewById(R.id.goButtonOrgName);
        final Button goEverythingElseButton = (Button) rootView.findViewById(R.id.goButtonEverythingElse);

        //add functionality to each object
        dateField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog.OnDateSetListener dpd = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        int s = monthOfYear+1;
                        String a = dayOfMonth+"/"+s+"/"+year;
                        dateField.setText(""+a);
                    }
                };

                Calendar mcurrentDate=Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getActivity(), dpd, mYear, mMonth, mDay);
                dialog.show();

            }
        });

        goButtonOrgName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        goEverythingElseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return rootView;
    }
}