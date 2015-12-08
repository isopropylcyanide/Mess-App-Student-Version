package com.example.cyanide.messapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import com.example.cyanide.messpp.R;


public class DietTracker extends Fragment {
    View homeview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeview = inflater.inflate(R.layout.diet_tracker, container, false);
        CalendarView cal = (CalendarView) homeview.findViewById(R.id.calendar);
        cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener(){

            public void onSelectedDayChange(CalendarView view ,int year,int month,int dayOfMonth)
            {

                Intent launchDateTracker;
                launchDateTracker = new Intent(getActivity(), Track.class);
                launchDateTracker.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                launchDateTracker.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                launchDateTracker.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(launchDateTracker);

            }
        });

        return homeview;
    }

}
