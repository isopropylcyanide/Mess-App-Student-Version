package com.example.cyanide.messapp.background;


public class Constants {

    public static final String DATABASE_URL = "https://sweltering-heat-4362.firebaseio.com/";
    public static final String USER_LOGIN_TABLE = "login_data/";
    public static final String PASSWORD_CHILD   = "password";
    public static final String SESSION_CHILD    = "session_valid";
    public static final Integer SESSION_TIMEOUT  = 15 * 60;  //15 min seconds
    public static final String LAST_LOGIN_CHILD = "last_login";

    public static final String USER_MESS_TABLE  = "user_mess_off/";
    public static final String USER_MESS_START_DATE_KEY = "start_date";
    public static final String USER_MESS_END_DATE_KEY = "end_date";
    public static final String USER_MESS_START_MEAL_KEY = "start_meal";
    public static final String USER_MESS_END_MEAL_KEY = "end_meal";

    //****Useful for diet tracking parts*****
    public static final String STUDENT_DIET_BFAST = "b";
    public static final String STUDENT_DIET_LUNCH = "l";
    public static final String STUDENT_DIET_DINNER = "d";
    public static final String STUDENT_DIET_EXTRAS = "extras";
    public static final String STUDENT_DIET_GUEST = "guest";

    public static final String STUDENT_DATABASE_DIET_TABLE = "roll_day_diet/";
    //Used in diet tracker

    /***** Holds the details of the user*/
    public static final String USER_PROFILE_TABLE = "user_student_data/";
    public static final String USER_PROFILE_ROOM = "Room No";
    public static final String USER_PROFILE_BRANCH = "Branch";
    public static final String USER_PROFILE_EMAIL = "Email ID";
    public static final String USER_PROFILE_FATHER = "Father's Name";
    public static final String USER_PROFILE_LAST_UPDATED = "Last Updated";
    public static final String USER_PROFILE_MOBILE = "Mobile";
    public static final String USER_PROFILE_NAME = "Name";
    public static final String USER_PROFILE_BLOOD = "Blood Group";
    public static final String USER_PROFILE_PARENT_MOBILE = "Parent's Mobile";

    public static final String DATE_FORMAT     = "EEE MMM dd hh:mm:ss zzz yyyy";
    public static final Long DIET_RECORD_MONTHS =6L;
    public static final Long MESS_OFF_MIN_DAYS = 2L;
    public static final Long MESS_OFF_MIN_NOTICE_DAY = 1L;
    public static final Long MESS_OFF_CANCEL_DAY = 2L;

    public static final Long MESS_OFF_MIN_TIME = MESS_OFF_MIN_DAYS * 86400L * 1000L;
    public static final Long MESS_OFF_MIN_NOTICE_TIME = MESS_OFF_MIN_NOTICE_DAY * 86400L * 1000L;
    public static final Long MESS_OFF_CANCEL_TIME = MESS_OFF_CANCEL_DAY * 86400L * 1000L;

    public static final String MESS_OFF_RULES = "\tMESS OFF RULES: " +
                                                "\n\nSet Off only when there is no current record. " +
            "Request reset if the record is to be deleted." +
                                                "\n\nDates entered should be valid with a duration not lesser than: "
            +MESS_OFF_MIN_DAYS.toString() +" day(s)." +
                                                "\n\nMinimum notice to be provided: "+ MESS_OFF_MIN_NOTICE_DAY+" day(s)." +
            "\n\nMess Off Record can be reset upto: "+MESS_OFF_CANCEL_DAY.toString() +" day(s) before the start date in the record." +
                                                "" ;

}
