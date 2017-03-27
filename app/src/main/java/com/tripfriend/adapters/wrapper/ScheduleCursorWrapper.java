package com.tripfriend.adapters.wrapper;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.util.Log;

import com.tripfriend.BaseActivity;
import com.tripfriend.model.Schedule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ScheduleCursorWrapper extends CursorWrapper {

    SimpleDateFormat sdf = new SimpleDateFormat(BaseActivity.DATE_HOUR_FORMAT, BaseActivity.APP_LOCALE);
    private final String TAG = "ScheduleCursorWrapper";

    public ScheduleCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Schedule getSchedule() {
        long id = getLong(getColumnIndex(Schedule.KEY_ID));
        int location = getInt(getColumnIndex(Schedule.KEY_LOCATION));
        int language = getInt(getColumnIndex(Schedule.KEY_LANGUAGE));
        int timespan = getInt(getColumnIndex(Schedule.KEY_TIMESPAN));
        int idFriend = getInt(getColumnIndex(Schedule.KEY_IDFRIEND));
        int group = getInt(getColumnIndex(Schedule.KEY_GROUP));
        int state = getInt(getColumnIndex(Schedule.KEY_STATE));

        String name = getString(getColumnIndex(Schedule.KEY_NAME));
        String surname = getString(getColumnIndex(Schedule.KEY_SURNAME));
        String email = getString(getColumnIndex(Schedule.KEY_EMAIL));
        String phone = getString(getColumnIndex(Schedule.KEY_PHONENUMBER));
        String pickup = getString(getColumnIndex(Schedule.KEY_PICKUPLOCATION));
        String notes = getString(getColumnIndex(Schedule.KEY_NAME));

        String date = getString(getColumnIndex(Schedule.KEY_CALENDARSTART));
        Calendar c = null;
        try {
            Date d = sdf.parse(date);

            c = Calendar.getInstance();
            c.setTime(d);
        } catch (ParseException e) {
            Log.e(TAG, e.getLocalizedMessage());
        }

        String p = getString(getColumnIndex(Schedule.KEY_PREFERENCES));
        String[] items = p.split(",");
        List<String> list = new ArrayList<String>();
        for(int i=0; i < items.length; i++){
            list.add(items[i]);
        }

        Schedule s = new Schedule(location, language, timespan, idFriend, group, c, name, surname, email, phone, pickup, notes, null, state);
        s.setId(id);
        return s;
    }

}
