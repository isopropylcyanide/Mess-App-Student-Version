package com.example.cyanide.messapp;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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

import com.example.cyanide.messapp.background.Constants;
import com.example.cyanide.messapp.background.StaticUserMap;
import com.example.cyanide.messpp.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

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
    private boolean recordExists;
    private String user_name;
    private HashMap<String, Object>existUser;

    private HashMap<String, String> mealNameMap;

    //An Async class that reads the recent messOff Statistic off the database
    //Check whether an entry in the mess Database exists or not
    //Display Status as (from_date): meal to {end_date}:meal
    private class fetchRecordAsync extends AsyncTask<Void, Void, Void>{
        private Context async_context;
        private ProgressDialog pd;
        private String roll;

        public fetchRecordAsync(Context context, String roll){
            this.async_context = context;
            pd = new ProgressDialog(async_context);
            this.roll = roll;
            recordExists = false;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Fetching Record");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            final Object lock = new Object();
            //refer to the currentMonth and currentDate

            ref.child(roll).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    synchronized (lock) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            recordExists = true;
                            if (ds.getKey() == Constants.USER_MESS_START_DATE_KEY)
                                existUser.put(ds.getKey(), ds.getValue().toString());

                            if (ds.getKey() == Constants.USER_MESS_END_DATE_KEY)
                                existUser.put(ds.getKey(), ds.getValue().toString());

                            if (ds.getKey() == Constants.USER_MESS_START_MEAL_KEY)
                                existUser.put(ds.getKey(), ds.getValue().toString());

                            if (ds.getKey() == Constants.USER_MESS_END_MEAL_KEY)
                                existUser.put(ds.getKey(), ds.getValue().toString());
                        }
                        lock.notifyAll();
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                }
            });

            synchronized (lock){
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //Handles the stuff after the synchronisation with the firebase listener has been achieved
            //The main UI is already idle by this moment

            super.onPostExecute(aVoid);
            if (recordExists){
                String toDisplay =
                            " From: " +existUser.get(Constants.USER_MESS_START_DATE_KEY) +
                                    "\t " +existUser.get(Constants.USER_MESS_START_MEAL_KEY) + " " +
                                    "\n To: " +existUser.get(Constants.USER_MESS_END_DATE_KEY) +
                                    "\t " +existUser.get(Constants.USER_MESS_END_MEAL_KEY) +  " " +
                                    "";
                    messStatus.setText(toDisplay);
                    mainStatus.setText("Mess Off : True");
            }
            else{
                messStatus.setText("No Record Exists");
                mainStatus.setText("Mess Off : False");
            }
            //Show the log in progress_bar for at least a few milliseconds
            Handler handler = new Handler();

            handler.postDelayed(new Runnable() {
                public void run() {
                    pd.dismiss();
                }
            }, 500);  // 100 milliseconds
        }
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
        myFormat = "yyyy-MM-dd";
        sdf = new SimpleDateFormat(myFormat, Locale.US);

        mealNameMap = new HashMap<String, String>();
        mealNameMap.put("Breakfast", Constants.STUDENT_DIET_BFAST);
        mealNameMap.put("Lunch", Constants.STUDENT_DIET_LUNCH);
        mealNameMap.put("Dinner", Constants.STUDENT_DIET_DINNER);

        fromSpinner.setSelection(0);
        toSpinner.setSelection(0);
        messStatus.setMovementMethod(new ScrollingMovementMethod());

        //Setup firebase to read at the required table. This is global to the activity
        //as the tableUrl remains constant throughout this session. But node may exist or not
        //depending on whether the user has set mess off or not
        user_name = StaticUserMap.getInstance()._roll;
        ref = new Firebase(Constants.DATABASE_URL + Constants.USER_MESS_TABLE);

        existUser = new HashMap<String, Object>();

        new fetchRecordAsync(getContext(), user_name).execute();

        //Display from date  to user in a readable format.
        fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mYear = startDate.get(Calendar.YEAR);
                int mMonth = startDate.get(Calendar.MONTH);
                int mDay = startDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedYear, int selectedMonth, int selectedDay) {
                        startDate.set(Calendar.YEAR, selectedYear);
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

        ref.child(user_name).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String , String> curMap = new HashMap<String, String>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    System.out.println("=> " + ds.getKey() + " -> " + ds.getValue());
                    if (ds.getKey() == Constants.USER_MESS_START_DATE_KEY)
                        curMap.put(ds.getKey(), ds.getValue().toString());

                    if (ds.getKey() == Constants.USER_MESS_END_DATE_KEY)
                        curMap.put(ds.getKey(), ds.getValue().toString());

                    if (ds.getKey() == Constants.USER_MESS_START_MEAL_KEY)
                        curMap.put(ds.getKey(), ds.getValue().toString());

                    if (ds.getKey() == Constants.USER_MESS_END_MEAL_KEY)
                        curMap.put(ds.getKey(), ds.getValue().toString());
                }

                if (curMap.size() == 0){
                    messStatus.setText("No Record Exists");
                    mainStatus.setText("Mess Off : False");
                    return;
                }

                String toDisplay =
                        " From: " +curMap.get(Constants.USER_MESS_START_DATE_KEY) +
                                "\t " +curMap.get(Constants.USER_MESS_START_MEAL_KEY) + " " +
                                "\n To: " +curMap.get(Constants.USER_MESS_END_DATE_KEY) +
                                "\t " +curMap.get(Constants.USER_MESS_END_MEAL_KEY) +  " " +
                                "";
                messStatus.setText(toDisplay);
                mainStatus.setText("Mess Off : True");
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        //Allow user to submit the necessary configuration. Confirm with an Alert Dialog.
        //Check for possible faults. If they exist,
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean valid_arg = true;
                final String from_meal = mealNameMap.get(fromSpinner.getSelectedItem().toString());
                final String to_meal = mealNameMap.get(toSpinner.getSelectedItem().toString());


                //if an entry already exists, don't continue
                if (recordExists){
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
//                else if ((startDate.getTime().getTime() -  new Date().getTime()) < Constants.MESS_OFF_MIN_NOTICE_TIME){
//                    Snackbar.make(getView(),"Diets have to be specified at least a day before.",Snackbar.LENGTH_SHORT).show();
//                    valid_arg = false;
//                }

                if (valid_arg){
                    //Request Confirmation to set mess off at the required times
                    new AlertDialog.Builder(getContext())
                            .setMessage("Are you sure you wish to proceed? This action is undoable once within: "+ Constants.MESS_OFF_CANCEL_DAY+" days before start time")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    existUser.put(Constants.USER_MESS_START_DATE_KEY, fromDate.getText().toString());
                                    existUser.put(Constants.USER_MESS_END_DATE_KEY, toDate.getText().toString());
                                    existUser.put(Constants.USER_MESS_START_MEAL_KEY, from_meal);
                                    existUser.put(Constants.USER_MESS_END_MEAL_KEY, to_meal);
                                    ref.child(user_name).updateChildren(existUser);
                                    recordExists = true;
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
                //if entry exists, extract the start date from the map
                //Allow entry to be deleted only if within the deletion bounds
                if (!recordExists) {
                    Snackbar.make(getView(), "No record exists", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                new AlertDialog.Builder(getContext())
                            .setMessage("Are you sure you wish to reset the record? This action is undoable")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    ref.child(user_name).removeValue();
                                    recordExists = false;
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
            }
        });
        return homeview;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        new AlertDialog.Builder(getContext())
//                .setMessage(Constants.MESS_OFF_RULES)
//                .setCancelable(false)
//                .setPositiveButton("I understand", null)
//                .show();
    }


}
