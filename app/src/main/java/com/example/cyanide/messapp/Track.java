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
    private TextView guestLabel, extrasLabel, rollText, roomText;
    private CheckBox bfast_cb,lunch_cb,dinner_cb;
    private View current;
    private Firebase extra_ref, guest_ref;
    private String _username;
    private String extrasTable, guestTable;

    private String month_str, date_str, year_str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        year = intent.getIntExtra("year", 0);
        month = intent.getIntExtra("month", 0);
        dayOfMonth = intent.getIntExtra("dayOfMonth", 0);

        current = this.findViewById(android.R.id.content);
        String [] date = new GregorianCalendar(year, month, dayOfMonth).getTime().toString().split(" ");
        month_str = date[1];
        date_str = date[2];
        year_str = Integer.toString(year);

        String to_display = date[0]+ " "+ date[1]+ " "+ date[2]+ " "+ year_str;
        setTitle(to_display);
        setContentView(R.layout.activity_track);

        guestLabel = (TextView)findViewById(R.id.guest_label);
        extrasLabel = (TextView)findViewById(R.id.extras_label);
        rollText = (TextView)findViewById(R.id.rollText);
        roomText = (TextView)findViewById(R.id.roomText);

        guestLabel.setEnabled(true);
        extrasLabel.setEnabled(true);

        bfast_cb     = (CheckBox)findViewById(R.id.bfast_cb);
        lunch_cb     = (CheckBox)findViewById(R.id.lunch_cb);
        dinner_cb    = (CheckBox)findViewById(R.id.dinner_cb);

        bfast_cb.setChecked(true);
        lunch_cb.setChecked(true);
        dinner_cb.setChecked(true);

        _username = StaticUserMap._userName;
        extrasTable = Constants.DATABASE_URL + Constants.USER_EXTRA_TABLE;
        guestTable  = Constants.DATABASE_URL + Constants.USER_GUEST_TABLE;

        extra_ref = new Firebase(extrasTable);
        guest_ref = new Firebase(guestTable);
        rollText.setText(_username);

        //Retrieve user extras cost at the desired day
        //if name exists in the database, and if the date matches that record

        //then display that in the label
        extra_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (_username.equals(ds.getKey())) {
                        ArrayList<HashMap<String, String> > test = (ArrayList<HashMap<String, String> >)ds.getValue();
                        extrasLabel.setText(test.get(dayOfMonth).get(Constants.USER_EXTRA_COST_KEY).toString());
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });


        //Retrieve guest diets at the specific date
        guest_ref.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (_username.equals(ds.getKey())) {
                        ArrayList<HashMap<String, String> > test = (ArrayList<HashMap<String, String> >)ds.getValue();
                        guestLabel.setText(test.get(dayOfMonth).get(Constants.USER_GUEST_COUNT_KEY).toString());
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

        current.setOnTouchListener(new View.OnTouchListener() {
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