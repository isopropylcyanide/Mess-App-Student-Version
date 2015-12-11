package com.example.cyanide.messapp.background;

import android.view.View;

import java.util.Map;

public class StaticUserMap {

    private static Map<String, Object> userStaticMap;
    private static Map<String, Object> UserViewExtras;
    private static boolean netConnected;
    public static String _userName;
    private View currentView;

    public View getCurrentView(){return currentView;}
    public void setCurrentView(View v){this.currentView = v;}

    public Map<String, Object> getUserViewExtras() {
        return UserViewExtras;
    }
    public void setUserViewExtras(Map<String, Object> userViewExtras) {UserViewExtras = userViewExtras; }

    public Map<String, Object> getUserMap(){
        return userStaticMap;
    }
    public void setUserMap(Map<String,Object> userStaticMap){this.userStaticMap = userStaticMap;}

    public boolean getConnectedStatus(){return netConnected;}
    public void setConnectedStatus(boolean flag){netConnected = flag;}

    public static StaticUserMap getInstance(){
        return holder;
    }
    private static final StaticUserMap holder = new StaticUserMap();}

