package com.example.cyanide.messapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cyanide.messpp.R;

/**
 * Created by cyanide on 12/9/15.
 */
public class home extends Fragment {
    View homeview;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeview = inflater.inflate(R.layout.home, container, false);
        return homeview;
    }


}
