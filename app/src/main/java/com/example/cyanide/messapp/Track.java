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

import com.example.cyanide.messpp.R;

public class Track extends AppCompatActivity {

    private int year, dayOfMonth, month;
    private TextView guestLabel, extrasLabel;
    private CheckBox bfast_cb,lunch_cb,dinner_cb;
    View current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);

        Intent intent = getIntent();
        year = intent.getIntExtra("year", 0);
        month = intent.getIntExtra("month", 0);
        dayOfMonth = intent.getIntExtra("year", 0);
        current = this.findViewById(android.R.id.content);

        guestLabel = (TextView)findViewById(R.id.guest_label);
        extrasLabel = (TextView)findViewById(R.id.extras_label);

        bfast_cb     = (CheckBox)findViewById(R.id.bfast_cb);
        lunch_cb     = (CheckBox)findViewById(R.id.lunch_cb);
        dinner_cb    = (CheckBox)findViewById(R.id.dinner_cb);
        bfast_cb.setChecked(true);
        lunch_cb.setChecked(true);
        dinner_cb.setChecked(true);

        //Retrieve User Details at the required date.

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