package com.tripfriend.model.configuration;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.tripfriend.model.Friend;
import com.tripfriend.model.Schedule;
import com.tripfriend.model.api.ApiService;
import com.tripfriend.utils.ArrayMapSeriabilize;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class LoadConfiguration {
    private final String config_url = "http://tf.sucharda.cz/tf-api/";
    private final String friend_url = "http://tf.sucharda.cz/tf-api/friends/";
    private final String friends_available_url = "http://tf.sucharda.cz/tf-api/friends-available/";
    private final String schedule_url = "http://tf.sucharda.cz/tf-api/schedule/";
    private final String schedule_create_url = "http://tf.sucharda.cz/tf-api/schedule/create/";
    private Context context;
    private ApiService apiService;

    private static final LoadConfiguration loadConfiguration = new LoadConfiguration();
    
    public static LoadConfiguration getInstance(Context context) {
        loadConfiguration.setContext(context);
        loadConfiguration.bindApiService(context);
        return loadConfiguration;
    }

    /*public void loadConfiguration() throws IOException, JSONException {
        JSONObject jsonObject = apiService.getJsonObject(config_url);

        JSONObject configObject = jsonObject.getJSONObject("config");

        // Sub main json object
        JSONObject locationsArray = configObject.getJSONObject("locations");
        JSONObject languagesArray = configObject.getJSONObject("languages");
        JSONObject timespansArray = configObject.getJSONObject("time_spans");
        JSONArray preferencesArray = configObject.getJSONArray("preferences");

        // Parse from object to array
        HashMap<Integer, String> locations = parseSingleArray(locationsArray);
        HashMap<Integer, String> languages = parseSingleArray(languagesArray);
        HashMap<Integer, String> timespans = parseSingleArray(timespansArray);
        List<String> preferences = parseSingleArrayNoID(preferencesArray);

        String start_time = configObject.getString("start_time");
        String end_time = configObject.getString("end_time");

        Configuration config = Configuration.getInstance();
        config.setLocations(locations);
        config.setLanguages(languages);
        config.setTime_spans(timespans);
        config.setPreferences(preferences);
        config.setStart_time(start_time);
        config.setEnd_time(end_time);
        config.setFriends(loadFriends());
        config.setIsSet(true);

        apiService.initiateDownload();
    }

    private List<Friend> loadFriends() throws IOException, JSONException {
        List<Friend> friends = new ArrayList<>();

        JSONObject jsonObject = apiService.getJsonObject(config_url);

        JSONObject friendsObject = jsonObject.getJSONObject("friends");
        JSONArray names = friendsObject.names();
        for(int i = 0; friendsObject.length() > i; i++ ) {
            String name = (String) names.get(i);
            JSONObject friendObject = friendsObject.getJSONObject(name);
            String thumbnail_url = friendObject.getString("image");
            String thumbnail = apiService.downloadFile(thumbnail_url);

            List<String> languages_url = parseSingleArrayNoID(friendObject.getJSONArray("languages"));
            String[] languages_url_parsed = languages_url.get(0).split(",");
            List<String> languages = apiService.downloadFiles(languages_url_parsed);

            friends.add(new Friend(friendObject.getInt("id"),
                    friendObject.getString("name"),
                    thumbnail,
                    "",
                    languages,
                    new Date()
            ));
        }
        return friends;
    }

    public HashMap<Integer, String> parseSingleArray(JSONObject jsonObject) throws IOException, JSONException {
        HashMap<Integer, String> returnArray = new HashMap<>();
        JSONArray names = jsonObject.names();

        // Change lists to Map<Integer, String>
        for(int i = 0; jsonObject.length() > i; i++) {
            String name = (String) names.get(i);
            Integer id = Integer.valueOf(name);
            returnArray.put(id, jsonObject.getString(name));
        }

        return returnArray;
    }

    public List<String> parseSingleArrayNoID(JSONArray jsonArray) throws IOException, JSONException {
        List<String> returnArray = new ArrayList<String>();

        for(int i = 0; jsonArray.length() > i; i++) {
            returnArray.add(jsonArray.getString(i));
        }

        return returnArray;
    }

    public void getAvailableFriends() throws IOException, JSONException {
        Schedule schedule = Schedule.getInstance();

        Calendar c = schedule.getCalendar_start();
        String date = getDateTime(c.getTime());

        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minutes = c.get(Calendar.MINUTE);
        int time_minutes = (hour*60) + minutes;

        JSONObject jsonSend = new JSONObject();
        jsonSend.put("location_id", schedule.getLocation());
        jsonSend.put("service_id", schedule.getLanguage());
        jsonSend.put("date", date);
        jsonSend.put("time", time_minutes);
        jsonSend.put("timespan", schedule.getTime_span());

        apiService.sendPost(0, friends_available_url, jsonSend);
    }

    public void completeOrder() throws IOException, JSONException {
        Schedule schedule = Schedule.getInstance();

        Calendar c = schedule.getCalendar_start();
        String date = getDateTime(c.getTime());

        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minutes = c.get(Calendar.MINUTE);
        int time_minutes = (hour*60) + minutes;

        String preferences = "";
        for( String pref : schedule.getPreferences() ) {
            preferences += pref + ",";
        }
        if(!preferences.equals("")) {
            preferences = preferences.substring(0, preferences.length() - 1);
        }

        JSONObject jsonSend = new JSONObject();
        jsonSend.put("location_id", schedule.getLocation());
        jsonSend.put("service_id", schedule.getLanguage());
        jsonSend.put("staff_id", schedule.getId_friend());
        jsonSend.put("date", date);
        jsonSend.put("time", time_minutes);
        jsonSend.put("timespan", schedule.getTime_span());
        jsonSend.put("preferences", preferences);

        jsonSend.put("pickup_location", schedule.getPickup_location());
        jsonSend.put("notes", schedule.getNotes());

        jsonSend.put("name", schedule.getName());
        jsonSend.put("surname", schedule.getSurname());
        jsonSend.put("email", schedule.getEmail());
        jsonSend.put("phone", schedule.getPhone_number());
        jsonSend.put("group", schedule.getGroup_count());

        apiService.sendPost(1, schedule_create_url, jsonSend);
    }

    private String getDateTime(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        return dateFormat.format(date);
    }

    public void getSchedules(String email) throws JSONException, IOException {
        JSONObject jsonSend = new JSONObject();
        //jsonSend.put("email", "john@cena.cena");
        jsonSend.put("email", email);

        apiService.sendPost(2, schedule_url, jsonSend);
    }*/

    /**
     * Load dummy configuration
     */
    public static void loadConfigurationDummy() {
        ArrayMapSeriabilize<Integer, String> locations = new ArrayMapSeriabilize<>();
        ArrayMapSeriabilize<Integer, String> languages = new ArrayMapSeriabilize<>();
        ArrayMapSeriabilize<Integer, String> timespans = new ArrayMapSeriabilize<>();
        List<String> preferences = new ArrayList<>();
        String start_time = "7:00";
        String end_time = "23:00";

        locations.put(1, "Prague");
        locations.put(2, "Amsterdam");
        locations.put(3, "New York");
        languages.put(1, "Czech");
        languages.put(2, "English");
        languages.put(3, "German");
        timespans.put(1, "3");
        timespans.put(2, "6");
        timespans.put(3, "10");
        preferences.add("Pub");
        preferences.add("Strip bar");
        preferences.add("Museums");
        preferences.add("Museums");
        preferences.add("Pub");
        preferences.add("Museums");
        preferences.add("Museums");
        preferences.add("Pub");
        preferences.add("Museums");
        preferences.add("Museums");
        preferences.add("Pub");
        preferences.add("Museums");
        preferences.add("Museums");
        preferences.add("Pub");
        preferences.add("Museums");
        preferences.add("Museums");
        preferences.add("Pub");
        preferences.add("Museums");
        preferences.add("Pub");
        preferences.add("Museums");

        Configuration config = Configuration.getInstance();
        config.setPreferences(preferences);
        config.setLocations(locations);
        config.setLanguages(languages);
        config.setTime_spans(timespans);
    }

    /**
     * Load dummy friends
     */
    public static void getFriendsDummy() {
        List<String> lang = new ArrayList<String>();
        lang.add("Czech");
        lang.add("English");
        lang.add("Polish");
        lang.add("German");

        List<Friend> friends = new ArrayList<>();

        for( int i = 0; i < 5; i++ ) {
            Friend f = new Friend(i,"Name"+i,"Image"+i,"Desc"+i, lang, new Date());
            friends.add(f);
        }

        Configuration config = Configuration.getInstance();
        config.setFriends(friends);
    }

    public static List<Schedule> getSchedulesDummy() {
        List<Schedule> schedules = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        List<String> preferences = new ArrayList<>();
        preferences.add("Neco");
        preferences.add("Pub");
        Schedule s = new Schedule(1, 2, 3, 3, 1, c, "Name", "Surname", "t@t.cs", "123456789", "Here and there", "Notes", preferences, 1);
        Schedule s2 = new Schedule(2, 3, 2, 2, 2, c, "David", "Testing", "test@supertest.com", "123456789", "North Pole", "Notes", preferences, 1);
        Schedule s3 = new Schedule(3, 4, 1, 4, 1, c, "Testing", "Some Surname", "t@supertest.csz", "123456789", "Nowhere", "Notes", preferences, 1);

        schedules.add(s);
        schedules.add(s2);
        schedules.add(s3);

        return schedules;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void bindApiService(Context context) { apiService = new ApiService(context); }
}
