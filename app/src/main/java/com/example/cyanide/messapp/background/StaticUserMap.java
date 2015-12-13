package com.example.cyanide.messapp.background;

import java.util.Map;

public class StaticUserMap {

    private static Map<String, Object> userStaticMap;
    private static boolean netConnected;
    public static String _userName;

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

