package com.example.cyanide.messapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import com.example.cyanide.messapp.background.Constants;
import com.example.cyanide.messapp.background.StaticUserMap;
import com.example.cyanide.messpp.R;
import com.firebase.client.Firebase;


public class DietTracker extends Fragment {
    View homeview;
    private long diet_track_interval;
    private CalendarView cal;
    //Mark those areas as red, where the user's mess is off

    private class easy_mess_viewer extends AsyncTask<Void, Void, Void>{
        private String node_url;
        private String user_name;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Get reference to the table
            user_name = StaticUserMap.getInstance()._userName;
            node_url = Constants.DATABASE_URL + Constants.USER_PROFILE_TABLE + user_name;

        }

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeview = inflater.inflate(R.layout.diet_tracker, container, false);
        cal = (CalendarView) homeview.findViewById(R.id.calendar);
        diet_track_interval = Constants.DIET_RECORD_MONTHS * 30L * 86400L * 1000L;

        cal.setMinDate(cal.getDate() - diet_track_interval);
        cal.setMaxDate(cal.getDate());

        //Start marking the dates as red accordingly
         new easy_mess_viewer().execute();

        cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener(){
            public void onSelectedDayChange(CalendarView view ,int year,int month,int dayOfMonth){
                Intent launchDateTracker;
                launchDateTracker = new Intent(getActivity(), Track.class);
                launchDateTracker.putExtra("year", year);
                launchDateTracker.putExtra("month", month);
                launchDateTracker.putExtra("dayOfMonth", dayOfMonth);
                startActivity(launchDateTracker);
            }
        });

        return homeview;
    }

}