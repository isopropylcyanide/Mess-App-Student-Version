package com.example.cyanide.messapp;

import java.util.Map;

public class StaticUserMap {

    private Map<String, Object> userStaticMap;
    private Map<String, Object> UserViewExtras;

    public Map<String, Object> getUserViewExtras() {
        return UserViewExtras;
    }

    public void setUserViewExtras(Map<String, Object> userViewExtras) {
        UserViewExtras = userViewExtras;
    }

    public Map<String, Object> getUserMap(){
        return userStaticMap;
    }

    public void setUserMap(Map<String,Object> userStaticMap){
        this.userStaticMap = userStaticMap;
    }

    public static StaticUserMap getInstance(){
        return holder;
    }
    private static final StaticUserMap holder = new StaticUserMap();}

