package com.example.cyanide.messapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cyanide.messapp.background.StaticUserMap;
import com.example.cyanide.messpp.R;


public class UserProfile extends Fragment {
    View homeview;
    private TextView name, branch, room, mobile, email, blood_group;
    private TextView f_name, f_mobile, lastUpdate;

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

        StaticUserMap curMap = StaticUserMap.getInstance();
        name.setText(curMap._name);
        branch.setText(curMap._branch);
        room.setText(curMap._roomNumber);
        mobile.setText(curMap._mobile);
        email.setText(curMap._email);
        blood_group.setText(curMap._blood);
        f_name.setText(curMap._father_name);
        f_mobile.setText(curMap._parent_mobile);
        lastUpdate.setText(curMap._last_updated);

        return homeview;
    }

}