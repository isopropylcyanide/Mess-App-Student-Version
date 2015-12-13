package com.example.cyanide.messapp;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cyanide.messapp.background.Constants;
import com.example.cyanide.messapp.background.StaticUserMap;
import com.example.cyanide.messpp.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class MessOff extends Fragment {
    private View homeview;
    private Spinner fromSpinner, toSpinner;
    private EditText fromDate, toDate;
    private TextView messStatus, mainStatus;
    private Button submit, reset;
    private Calendar startDate, endDate;
    private String myFormat;
    private SimpleDateFormat sdf;

    private Firebase ref;
    private String table_url;
    private boolean entryExists;
    private String user_name;
    private ValueEventListener valueEventListener;
    private HashMap<String, Object>existUser;

    //An Async class that reads the recent messOff Statistic off the database
    //Check whether an entry in the mess Database exists or not
    //Display Status as (from_date): meal to {end_date}:meal

    private void update_dates_with_times(Calendar cal, String meal_time){
        int hour = 0;
        if (meal_time.equals("Breakfast")) hour = 7;
        else if (meal_time.equals("Lunch")) hour = 12;
        else if (meal_time.equals("Dinner")) hour = 20;
        cal.set(Calendar.HOUR_OF_DAY,hour);
        cal.set(Calendar.MINUTE,30);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        System.out.println("I am in messoff onCreateView");
        homeview = inflater.inflate(R.layout.mess_off, container, false);

        fromDate = (EditText)homeview.findViewById(R.id.fromdate);
        toDate  = (EditText)homeview.findViewById(R.id.todate);
        mainStatus = (TextView)homeview.findViewById(R.id.mainStatus);

        fromSpinner = (Spinner)homeview.findViewById(R.id.fromSpinner);
        toSpinner = (Spinner)homeview.findViewById(R.id.toSpinner);
        messStatus = (TextView)homeview.findViewById(R.id.messStatus);
        submit = (Button)homeview.findViewById(R.id.submitBtn);

        reset =  (Button)homeview.findViewById(R.id.resetBtn);
        startDate = Calendar.getInstance();
        endDate = Calendar.getInstance();
        myFormat = "dd/MM/yy";
        entryExists = false;
        sdf = new SimpleDateFormat(myFormat, Locale.US);

        fromSpinner.setSelection(0);
        toSpinner.setSelection(0);
        messStatus.setMovementMethod(new ScrollingMovementMethod());

        //Setup firebase to read at the required table. This is global to the activity
        //as the tableUrl remains constant throughout this session. But node may exist or not
        //depending on whether the user has set mess off or not
        user_name = StaticUserMap.getInstance()._userName;
        table_url = Constants.DATABASE_URL + Constants.USER_MESS_TABLE ;
        ref = new Firebase(table_url);

        //Fetch current Mess Status and put it into Mess Status display
        //Continue fetching until activity finishes.
        valueEventListener = ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                entryExists = false;

                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    if (user_name.equals(ds.getKey())){
                        entryExists = true;
                        existUser = (HashMap<String, Object>) ds.getValue();
                        break;
                    }
                }

                if (entryExists){
                    String toDisplay =
                            " From: " +existUser.get(Constants.USER_MESS_START_DATE_KEY) +
                                    "\t " +existUser.get(Constants.USER_MESS_START_MEAL_KEY) + " " +
                                    "\n To: " +existUser.get(Constants.USER_MESS_END_DATE_KEY) +
                                    "\t " +existUser.get(Constants.USER_MESS_END_MEAL_KEY) +  " " +
                                    "";
                    messStatus.setText(toDisplay);
                    mainStatus.setText("Mess Off : True");
                }
                else {
                    entryExists = false;
                    messStatus.setText("No Record Exists");
                    mainStatus.setText("Mess Off : False");
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

        //Display from date  to user in a readable format.
        fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mYear = startDate.get(Calendar.YEAR);
                int mMonth = startDate.get(Calendar.MONTH);
                int mDay = startDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedYear, int selectedMonth, int selectedDay) {
                        startDate.set(Calendar.YEAR,selectedYear);
                        startDate.set(Calendar.MONTH, selectedMonth);
                        startDate.set(Calendar.DAY_OF_MONTH, selectedDay);
                        fromDate.setText(sdf.format(startDate.getTime()));
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.getDatePicker().setCalendarViewShown(false);
                mDatePicker.show();
            }
        });

        //Display end date to user in a readable format
        toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mYear = endDate.get(Calendar.YEAR);
                int mMonth = endDate.get(Calendar.MONTH);
                int mDay = endDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedYear, int selectedMonth, int selectedDay) {
                        endDate.set(Calendar.DAY_OF_MONTH,selectedDay);
                        endDate.set(Calendar.MONTH,selectedMonth);
                        endDate.set(Calendar.YEAR,selectedYear);
                        toDate.setText(sdf.format(endDate.getTime()));
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.getDatePicker().setCalendarViewShown(false);
                mDatePicker.show();
            }
        });

        //Allow user to submit the necessary configuration. Confirm with an Alert Dialog.
        //Check for possible faults. If they exist,

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!StaticUserMap.getInstance().getConnectedStatus()){
                    Snackbar.make(getView(),"Connection Failed. Please Try Again Later.",Snackbar.LENGTH_SHORT).show();
                    return;
                }

                boolean valid_arg = true;
                final String from_meal = fromSpinner.getSelectedItem().toString();
                final String to_meal = toSpinner.getSelectedItem().toString();

                update_dates_with_times(startDate, from_meal);
                update_dates_with_times(endDate, to_meal);

                //if an entry already exists, don't continue
                if (entryExists){
                    Snackbar.make(getView(),"Mess off record Exists already. Request Reset to delete mess off",Snackbar.LENGTH_SHORT).show();
                    valid_arg = false;
                }

                //if dates are not selected, don't proceed
                else if (fromDate.getText().toString().equals("") || toDate.getText().toString().equals("")){
                    Snackbar.make(getView(),"Null dates aren't allowed.",Snackbar.LENGTH_SHORT).show();
                    valid_arg = false;
                }

                //else if their duration is less than the minimum, it is invalid
                else if ((endDate.getTime().getTime() - startDate.getTime().getTime()) < Constants.MESS_OFF_MIN_TIME){
                    Snackbar.make(getView(), "Date duration is either invalid or less than minimum.", Snackbar.LENGTH_SHORT).show();
                    valid_arg = false;
                }

                //and even so if the start date is not at least a day ahead
                 else if ((startDate.getTime().getTime() -  new Date().getTime()) < Constants.MESS_OFF_MIN_NOTICE_TIME){
                    Snackbar.make(getView(),"Diets have to be specified at least a day before.",Snackbar.LENGTH_SHORT).show();
                    valid_arg = false;
                }

                if (valid_arg){
                    //Request Confirmation to set mess off at the required times
                    new AlertDialog.Builder(getContext())
                            .setMessage("Are you sure you wish to proceed? This action is undoable once within: "+ Constants.MESS_OFF_CANCEL_DAY+" days before start time")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    HashMap<String, Object> newMessEntry = new HashMap<String, Object>();
                                    HashMap<String, String> messOffDetails = new HashMap<String, String>();

                                    messOffDetails.put(Constants.USER_MESS_START_DATE_KEY,startDate.getTime().toString());
                                    messOffDetails.put(Constants.USER_MESS_END_DATE_KEY, endDate.getTime().toString());
                                    messOffDetails.put(Constants.USER_MESS_START_MEAL_KEY,from_meal);
                                    messOffDetails.put(Constants.USER_MESS_END_MEAL_KEY,to_meal);
                                    newMessEntry.put(user_name, messOffDetails);
                                    ref.updateChildren(newMessEntry);
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                }
            }
        });

        //Allow user to reset the Mess Off configuration. Confirm with an alert dialog.
        //Show the changes in the Mess Status

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!StaticUserMap.getInstance().getConnectedStatus()){
                    Snackbar.make(getView(),"Internet Disconnected. Try Again",Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (!entryExists)
                    Snackbar.make(getView(),"Internet Disconnected. Try Again",Snackbar.LENGTH_SHORT).show();

                else{
                    //if entry exists, extract the start date from the map
                    //Allow entry to be deleted only if within the deletion bounds

                    String startRecord = existUser.get(Constants.USER_MESS_START_DATE_KEY).toString();
                    SimpleDateFormat formatter = new SimpleDateFormat(Constants.DATE_FORMAT);
                    Date startRecordDate = new Date();
                    try {
                        startRecordDate = formatter.parse(startRecord);
                    } catch (ParseException e) {
                        messStatus.setText("Couldn't parse date. Report Error");
                        mainStatus.setText("Error");
                    }

                    if((startRecordDate.getTime() - new Date().getTime()) < Constants.MESS_OFF_CANCEL_TIME){
                        Snackbar.make(getView(),"Cannot reset record now. Interval exceeded.",Snackbar.LENGTH_SHORT).show();
                    }
                    else {
                        new AlertDialog.Builder(getContext())
                                .setMessage("Are you sure you wish to reset the record? This action is undoable")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        ref.child(user_name).removeValue();
                                    }
                                })
                                .setNegativeButton("No", null)
                                .show();
                    }
                }
            }
        });
        return homeview;
    }

    @Override
    public void onAttach(Context context) {
        System.out.println("Damn its attached");
        super.onAttach(context);
        new AlertDialog.Builder(getContext())
                .setMessage(Constants.MESS_OFF_RULES)
                .setCancelable(false)
                .setPositiveButton("I understand", null)
                .show();
    }

    @Override
    public void onPause() {
        super.onPause();
        ref.removeEventListener(valueEventListener);
    }

}
