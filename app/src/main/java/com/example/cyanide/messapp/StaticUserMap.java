package com.example.cyanide.messapp;

import java.util.Map;

public class StaticUserMap {

    private Map<String, Object> userStaticMap;
    private Map<String, Object> UserViewExtras;
    private boolean netConnected;
    protected String _userName;


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

