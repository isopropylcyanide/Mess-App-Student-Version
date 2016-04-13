package com.example.cyanide.messapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cyanide.messapp.background.Constants;
import com.example.cyanide.messapp.background.StaticUserMap;
import com.example.cyanide.messpp.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

/**
 * Created by cyanide on 12/9/15.
 */
public class Home extends Fragment {
    View homeview;
    private TextView lastUserUpdate;
    private Firebase roomNumberFetcher;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeview = inflater.inflate(R.layout.home, container, false);
        lastUserUpdate  = (TextView)homeview.findViewById(R.id.lastUserUpdate);
        //Incrementally fetch details of the user

        String userName = StaticUserMap.getInstance()._roll;
        roomNumberFetcher = new Firebase(Constants.DATABASE_URL + Constants.USER_PROFILE_TABLE).child(userName);

        roomNumberFetcher.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    System.out.println(ds.getKey() + ": " + ds.getValue());
                    if (ds.getKey() == Constants.USER_PROFILE_ROOM)
                        StaticUserMap.getInstance()._roomNumber = ds.getValue().toString();

                    else if (ds.getKey() == Constants.USER_PROFILE_BRANCH)
                        StaticUserMap.getInstance()._branch = ds.getValue().toString();

                    else if (ds.getKey() == Constants.USER_PROFILE_ROOM)
                        StaticUserMap.getInstance()._roomNumber = ds.getValue().toString();

                    else if (ds.getKey() == Constants.USER_PROFILE_MOBILE)
                        StaticUserMap.getInstance()._mobile = ds.getValue().toString();

                    else if (ds.getKey() == Constants.USER_PROFILE_EMAIL)
                        StaticUserMap.getInstance()._email = ds.getValue().toString();

                    else if (ds.getKey() == Constants.USER_PROFILE_FATHER)
                        StaticUserMap.getInstance()._father_name = ds.getValue().toString();

                    else if (ds.getKey() == Constants.USER_PROFILE_BLOOD)
                        StaticUserMap.getInstance()._blood = ds.getValue().toString();

                    else if (ds.getKey() == Constants.USER_PROFILE_PARENT_MOBILE)
                        StaticUserMap.getInstance()._parent_mobile = ds.getValue().toString();

                    else if (ds.getKey() == Constants.USER_PROFILE_NAME)
                        StaticUserMap.getInstance()._name = ds.getValue().toString();

                    else if (ds.getKey() == Constants.USER_PROFILE_LAST_UPDATED) {
                        StaticUserMap.getInstance()._last_updated = ds.getValue().toString();
                        lastUserUpdate.setText(ds.getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        return homeview;
    }


}
