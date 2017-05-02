package com.example.ee461lproject;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;
import java.util.List;


/**
 * Created by Aaron on 5/1/17.
 */

@SuppressWarnings("FieldCanBeLocal")
class EventFeedAdapter extends ArrayAdapter<Event> {

    private TextView feed_EventName;
    private TextView feed_EventDate;
    private TextView feed_EventTime;
    private ImageView categoryImage;
    private static final String TAG = "EventFeedAdapter";
    private static final String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul",
                                            "Aug", "Sep", "Oct", "Nov", "Dec"};
    private String month;
    private String year;

    public EventFeedAdapter(@NonNull Context context, @NonNull List<Event> events) {
        super(context, R.layout.feed_event_item, events);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater eventInflater = LayoutInflater.from(getContext());
        // TODO: Implement View Holder pattern
        View eventItemView = eventInflater.inflate(R.layout.feed_event_item, parent, false);
        Event e = getItem(position);

        // This should result in a String array with 6 elements.
        // According to the docs, the format is as follows: dow mon dd hh:mm:ss zzz yyyy
            // dow = day of week
            // mon = month
            // dd = day of the month (01 through 31)
            // hh:mm:ss = time using 24-hour clock
            // zzz = time zone
            // yyyy = year
        String[] dateFields = e.getDate().toString().split(" ");
        String time = "";
        String dayOfMonth = "";
        String date = "";
        month = "";
        year = "";

        if (dateFields != null) {

            setMonthAndYear(dateFields[1], dateFields[5]);
            dayOfMonth = dateFields[2].trim();
            time = dateFields[3].trim();

            // Should display date of form: Mon. DD, YYYY
                // e.g. Mar. 15, 2017
                // e.g. Feb. 03, 2017
            date = month + " " + dayOfMonth + ", " + year;
            Log.d(TAG, "the date string: " + date);
        }
        else {
            Log.d(TAG, "dateFields == null");
        }

        feed_EventName = (TextView) eventItemView.findViewById(R.id.feed_EventName);
        feed_EventDate = (TextView) eventItemView.findViewById(R.id.feed_EventDate);
        feed_EventTime = (TextView) eventItemView.findViewById(R.id.feed_EventTime);
        categoryImage = (ImageView) eventItemView.findViewById(R.id.categoryImage);

        feed_EventName.setText(e.getEventName());
        feed_EventDate.setText(date);
        feed_EventTime.setText(time);

        feed_EventName.setTextColor(Color.WHITE);
        feed_EventDate.setTextColor(Color.WHITE);
        feed_EventTime.setTextColor(Color.WHITE);

        categoryImage.setImageResource(R.drawable.social_icon);

        return eventItemView;
    }

    private void setMonthAndYear(String origMonth, String origYear) {
        int monthIndex;
        for (monthIndex = 0; monthIndex < 12; monthIndex++) {
            if (origMonth.equals(months[monthIndex])) {
                break;
            }
        }

        int adjustedYear = 0;
        monthIndex = monthIndex - 1;
        if (monthIndex < 0) {
            monthIndex += 12;
            adjustedYear = Integer.parseInt(origYear) - 1901;
        }
        else {
            adjustedYear = Integer.parseInt(origYear) - 1900;
        }
        year = String.valueOf(adjustedYear);
        month = months[monthIndex] + ".";
    }
}
