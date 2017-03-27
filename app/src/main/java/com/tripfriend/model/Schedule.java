package com.tripfriend.model;

import android.content.ContentValues;

import com.tripfriend.BaseActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Schedule {

    // Table name
    public static String TABLE = "schedule";
    // Int variables
    public static String KEY_ID = "_id";
    public static String KEY_LOCATION = "location";
    public static String KEY_LANGUAGE = "language";
    public static String KEY_TIMESPAN = "time_span";
    public static String KEY_IDFRIEND = "id_friend";
    public static String KEY_GROUP = "group_count";
    public static String KEY_STATE = "state";
    // String variables
    public static String KEY_NAME = "name";
    public static String KEY_SURNAME = "surname";
    public static String KEY_EMAIL = "email";
    public static String KEY_PHONENUMBER = "phone_number";
    public static String KEY_PICKUPLOCATION = "pickup_location";
    public static String KEY_NOTES = "notes";
    // Object variables
    public static String KEY_CALENDARSTART = "calendar_start";
    public static String KEY_PREFERENCES = "preferences";

    private long _id;
    private int location, language, time_span, id_friend, group_count, state;
    private Calendar calendar_start;
    private String name, surname, email, phone_number, pickup_location, notes;
    private List<String> preferences;

    SimpleDateFormat sdf = new SimpleDateFormat(BaseActivity.DATE_HOUR_FORMAT, BaseActivity.APP_LOCALE);

    private static Schedule schedulePlan = new Schedule();
    public static Schedule getPlanInstance() { return schedulePlan; }
    public static Schedule resetPlanInstance() {
        schedulePlan = new Schedule();
        return schedulePlan;
    }
    public static void forcePlanInstance(Schedule s) {
        schedulePlan = s;
    }

    private static final Schedule scheduleFind = new Schedule();
    public static Schedule getFindInstance() { return scheduleFind; }

    public Schedule(int location, int language, int time_span, int id_friend, int group_count, Calendar calendar_start, String name, String surname, String email, String phone_number, String pickup_location, String notes, List<String> preferences, int state) {
        this.location = location;
        this.language = language;
        this.time_span = time_span;
        this.id_friend = id_friend;
        this.group_count = group_count;
        this.calendar_start = calendar_start;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phone_number = phone_number;
        this.preferences = preferences;
        this.pickup_location = pickup_location;
        this.notes = notes;
        this.state = state;
    }

    public Schedule(){
        this.preferences = new ArrayList<String>();
        this.location = -1;
        this.language = -1;
        this.time_span = -1;
        this.state = 1;
    }

    public void resetSchedule() {
        this.location = -1;
        this.language = -1;
        this.time_span = -1;
        this.id_friend = -1;
        this.state = 1;
        this.calendar_start = null;
        this.preferences = new ArrayList<String>();
    }

    public long getId() {
        return _id;
    }

    public void setId(long id) {
        this._id = id;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public int getLanguage() {
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    public int getTime_span() {
        return time_span;
    }

    public void setTime_span(int time_span) {
        this.time_span = time_span;
    }

    public int getId_friend() {
        return id_friend;
    }

    public void setId_friend(int id_friend) {
        this.id_friend = id_friend;
    }

    public int getGroup_count() {
        return group_count;
    }

    public void setGroup_count(int group_count) {
        this.group_count = group_count;
    }

    public Calendar getCalendar_start() {
        return calendar_start;
    }

    public void setCalendar_start(Calendar calendar_start) {
        this.calendar_start = calendar_start;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getPickup_location() {
        return pickup_location;
    }

    public void setPickup_location(String pickup_location) {
        this.pickup_location = pickup_location;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<String> getPreferences() {
        return preferences;
    }

    public void setPreferences(List<String> preferences) {
        this.preferences = preferences;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        // Int
        contentValues.put(Schedule.KEY_LOCATION, location);
        contentValues.put(Schedule.KEY_LANGUAGE, language);
        contentValues.put(Schedule.KEY_TIMESPAN, time_span);
        contentValues.put(Schedule.KEY_IDFRIEND, id_friend);
        contentValues.put(Schedule.KEY_GROUP, group_count);
        contentValues.put(Schedule.KEY_STATE, state);
        // String
        contentValues.put(Schedule.KEY_NAME, name);
        contentValues.put(Schedule.KEY_SURNAME, surname);
        contentValues.put(Schedule.KEY_EMAIL, email);
        contentValues.put(Schedule.KEY_PHONENUMBER, phone_number);
        contentValues.put(Schedule.KEY_PICKUPLOCATION, pickup_location);
        contentValues.put(Schedule.KEY_NOTES, notes);
        // Calendar
        String dateTime = sdf.format( calendar_start.getTime() );
        contentValues.put(Schedule.KEY_CALENDARSTART, dateTime);
        // List
        String preferencesData = "";
        if(preferences.size() > 0) {
            StringBuilder friendsList = new StringBuilder();
            for (String s : preferences) {
                friendsList.append(s);
                friendsList.append(",");
            }
            friendsList.deleteCharAt(friendsList.length() - 1);
            preferencesData = friendsList.toString();
        }
        contentValues.put(Schedule.KEY_PREFERENCES, preferencesData);
        return contentValues;
    }

}
