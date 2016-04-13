package com.example.cyanide.messapp.background;

public class StaticUserMap {

    private static boolean netConnected;
    public static String _roll;
    public static String _name;
    public static String _roomNumber;
    public static String _branch, _mobile, _father_name;
    public static String _parent_mobile, _last_updated, _blood, _email;

    private static String _password;//private Data

    public String get_password(){
        return _password;
    }

    public void set_password(String pass){
        _password = new String(pass);
    }

    public boolean getConnectedStatus(){return netConnected;}
    public void setConnectedStatus(boolean flag){netConnected = flag;}

    public static StaticUserMap getInstance(){
        return holder;
    }
    private static final StaticUserMap holder = new StaticUserMap();}

