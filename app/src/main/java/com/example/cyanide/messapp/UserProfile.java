package com.example.cyanide.messapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cyanide.messapp.background.Constants;
import com.example.cyanide.messapp.background.StaticUserMap;
import com.example.cyanide.messpp.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Map;


public class UserProfile extends Fragment {
    View homeview;
    private TextView name, branch, room, mobile, email, blood_group;
    private TextView f_name, f_mobile, lastUpdate;
    private boolean netConnected;

    private class Extract_data_async extends AsyncTask<Void, Void, Void> {

        private Firebase ref;
        private Map<String, Object> userChange,user_args;
        private String node_url;
        private String user_name;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            user_name = StaticUserMap.getInstance()._userName;
            node_url = Constants.DATABASE_URL + Constants.USER_PROFILE_TABLE + '/' + user_name + '/';
            ref = new Firebase(node_url);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }


        @Override
        protected Void doInBackground(Void... params) {

            final Object lock = new Object();

            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                TextView text_set = null;

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    synchronized (lock) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            if      (ds.getKey() == "Name")     text_set = name;
                            else if (ds.getKey() == "Branch")   text_set = branch;
                            else if (ds.getKey() == "Room No")  text_set = room;
                            else if (ds.getKey() == "Mobile")   text_set = mobile;
                            else if (ds.getKey() == "Email ID")         text_set = email;
                            else if (ds.getKey() == "Blood Group")      text_set = blood_group;
                            else if (ds.getKey() == "Parent's Mobile")  text_set = f_mobile ;
                            else if (ds.getKey() == "Father's Name")    text_set = f_name;
                            else if (ds.getKey() == "Last Updated")     text_set = lastUpdate;
                            if(text_set != null)
                                text_set.setText(ds.getValue().toString());
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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeview = inflater.inflate(R.layout.user_profile, container, false);

        name          = (TextView)homeview.findViewById(R.id.Prof_Name);
        branch        = (TextView)homeview.findViewById(R.id.Prof_Branch);
        room          = (TextView)homeview.findViewById(R.id.Prof_Room);
        mobile        = (TextView)homeview.findViewById(R.id.Prof_Mobile);
        email         = (TextView)homeview.findViewById(R.id.Prof_Email);
        blood_group   = (TextView)homeview.findViewById(R.id.Prof_Blood);
        f_name        = (TextView)homeview.findViewById(R.id.Prof_Father);
        f_mobile      = (TextView)homeview.findViewById(R.id.Prof_Father_Mobile);
        lastUpdate    = (TextView)homeview.findViewById(R.id.Prof_Last_Update);

        netConnected  = StaticUserMap.getInstance().getConnectedStatus();

        if(!netConnected)
            Toast.makeText(getActivity().getApplicationContext(), "Internet Disconnected. Try Again", Toast.LENGTH_SHORT).show();

        else new Extract_data_async().execute();


        return homeview;
    }

}