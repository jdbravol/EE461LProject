package com.example.ee461lproject;

import android.app.DatePickerDialog;
import android.app.TabActivity;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by juanbravo on 4/17/17.
 */

public class Student_Tab1_SearchEvents extends Fragment {

    private static final String TAG = "Student_Tab1";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        //set view
        View rootView =  inflater.inflate(R.layout.tab1_search_events, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        final StudentOptions parent = (StudentOptions) getActivity();

        //Link objects in fragment
        final EditText dateField = (EditText) rootView.findViewById(R.id.dateField);
        final EditText orgName = (EditText) rootView.findViewById(R.id.orgNameField);
        final EditText category = (EditText) rootView.findViewById(R.id.categoryField);
        final Button goButtonOrgName = (Button) rootView.findViewById(R.id.goButtonOrgName);
        final Button goEverythingElseButton = (Button) rootView.findViewById(R.id.goButtonEverythingElse);
        final CheckBox freeFoodBox = (CheckBox) rootView.findViewById(R.id.checkBox);
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
                Log.d(TAG, "entering goButtonOrgName's onClick method");

                StudentOptions.searchMode.set(true);

                parent.filterAdapterByOrg(orgName.getText().toString().trim());
                Button backToMainFeed = (Button) parent.findViewById(R.id.backToMainFeed);
                backToMainFeed.setVisibility(View.VISIBLE);

                TabLayout tabLayout = (TabLayout) parent.findViewById(R.id.tabs);
                parent.changeTabName("Results");
                tabLayout.getTabAt(1).select();


                Log.d(TAG, "leaving goButtonOrgName's onClick method");
            }
        });

        goEverythingElseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StudentOptions.searchMode.set(true);

                parent.filterAdapterByEverythingElse(category.getText().toString(), dateField.getText().toString(), freeFoodBox.isChecked());
                Button backToMainFeed = (Button) parent.findViewById(R.id.backToMainFeed);
                backToMainFeed.setVisibility(View.VISIBLE);

                TabLayout tabLayout = (TabLayout) parent.findViewById(R.id.tabs);
                parent.changeTabName("Results");
                tabLayout.getTabAt(1).select();
            }
        });

        return rootView;
    }
}