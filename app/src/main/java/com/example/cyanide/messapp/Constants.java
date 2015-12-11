package com.example.cyanide.messapp;


public class Constants {

    public static final String DATABASE_URL = "https://sweltering-heat-4362.firebaseio.com/";
    public static final String USER_LOGIN_TABLE = "login_data";
    public static final String PASSWORD_CHILD   = "password";
    public static final String SESSION_CHILD    = "session_valid";
    public static final String LAST_LOGIN_CHILD = "last_login";
    public static final Integer SESSION_TIMEOUT  = 15 * 60;  //15 min seconds
    public static final String DATE_FORMAT     = "EEE MMM dd hh:mm:ss zzz yyyy";

}
