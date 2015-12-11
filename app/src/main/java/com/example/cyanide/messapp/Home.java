package com.example.cyanide.messapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.cyanide.messapp.background.StaticUserMap;
import com.example.cyanide.messpp.R;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.logging.SimpleFormatter;

/**
 * Created by cyanide on 12/9/15.
 */
public class Home extends Fragment {
    View homeview;
    private TextView lastUserUpdate;
    private Map<String, Object> userChange;
    private String last_login_child;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeview = inflater.inflate(R.layout.home, container, false);

        userChange = StaticUserMap.getInstance().getUserMap();
        last_login_child  = StaticUserMap.getInstance().getUserViewExtras().get("EXTRA_Node_Last_Log_Field").toString();
        lastUserUpdate  = (TextView)homeview.findViewById(R.id.lastUserUpdate);
        String last_log_date = userChange.get(last_login_child).toString();

        String []arr = last_log_date.split(" ");
        String to_display = "";
        for(int a =0; a<4; a++){to_display += arr[a] + " ";}
        lastUserUpdate.setText(to_display);
        return homeview;
    }


}
