package com.example.cyanide.messapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.cyanide.messapp.background.Constants;
import com.example.cyanide.messapp.background.StaticUserMap;
import com.example.cyanide.messpp.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class Track extends AppCompatActivity {

    private int year, dayOfMonth, month;
    private TextView guestsEt, extrasEt, rollText, roomText;
    private CheckBox bfastCb, lunchCb, dinnerCb;
    private View currentView;
    private Firebase studentRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        year = intent.getIntExtra("year", 0);
        month = intent.getIntExtra("month", 1) + 1; //month is 0 based
        dayOfMonth = intent.getIntExtra("dayOfMonth", 0);
        studentRef = new Firebase(intent.getStringExtra("firebaseRef"));



        currentView = this.findViewById(android.R.id.content);
        String [] date = new GregorianCalendar(year, month -1, dayOfMonth).getTime().toString().split(" ");
        String to_display = date[0]+ " "+ date[1]+ " "+ date[2]+ " "+ Integer.toString(year);

        setTitle(to_display);
        setContentView(R.layout.activity_track);

        guestsEt = (TextView)findViewById(R.id.guest_label);
        extrasEt = (TextView)findViewById(R.id.extras_label);
        rollText = (TextView)findViewById(R.id.rollText);
        roomText = (TextView)findViewById(R.id.roomText);
        bfastCb = (CheckBox)findViewById(R.id.bfast_cb);
        lunchCb = (CheckBox)findViewById(R.id.lunch_cb);
        dinnerCb = (CheckBox)findViewById(R.id.dinner_cb);

        guestsEt.setClickable(false);
        extrasEt.setClickable(false);
        bfastCb.setClickable(false);
        lunchCb.setClickable(false);
        dinnerCb.setClickable(false);

        bfastCb.setChecked(true);
        lunchCb.setChecked(true);
        dinnerCb.setChecked(true);

        rollText.setText(studentRef.getKey());
        roomText.setText(StaticUserMap.getInstance()._roomNumber);

        studentRef.child(year +"").child(month +"").child(dayOfMonth +"").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.getKey().equals(Constants.STUDENT_DIET_BFAST))
                        bfastCb.setChecked((Boolean) ds.getValue());

                    else if (ds.getKey().equals(Constants.STUDENT_DIET_LUNCH))
                        lunchCb.setChecked((Boolean) ds.getValue());

                    else if (ds.getKey().equals(Constants.STUDENT_DIET_DINNER))
                        dinnerCb.setChecked((Boolean) ds.getValue());

                    else if (ds.getKey().equals(Constants.STUDENT_DIET_EXTRAS))
                        extrasEt.setText(ds.getValue().toString());

                    else if (ds.getKey().equals(Constants.STUDENT_DIET_GUEST))
                        guestsEt.setText(ds.getValue().toString());
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });


        //Press anywhere to go back
        currentView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                onBackPressed();
                return false;
            }
        });
    }

    @Override
    public void onBackPressed(){
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}