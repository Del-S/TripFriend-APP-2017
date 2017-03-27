package com.tripfriend.model.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.tripfriend.model.Schedule;
import com.tripfriend.model.configuration.LoadConfiguration;

import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 16;
    private static final String DATABASE_NAME = "schedules.db";
    private final Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //All necessary tables you like to create will create here
        String CREATE_TABLE_GAME = "CREATE TABLE " + Schedule.TABLE + '('
                + Schedule.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + Schedule.KEY_LOCATION + " INTEGER, "
                + Schedule.KEY_LANGUAGE + " INTEGER, "
                + Schedule.KEY_TIMESPAN + " INTEGER, "
                + Schedule.KEY_IDFRIEND + " INTEGER, "
                + Schedule.KEY_GROUP + " INTEGER, "
                + Schedule.KEY_STATE + " INTEGER, "
                + Schedule.KEY_NAME + " TEXT, "
                + Schedule.KEY_SURNAME + " TEXT, "
                + Schedule.KEY_EMAIL + " TEXT, "
                + Schedule.KEY_PHONENUMBER + " TEXT, "
                + Schedule.KEY_PICKUPLOCATION + " TEXT, "
                + Schedule.KEY_NOTES + " TEXT, "
                + Schedule.KEY_CALENDARSTART + " TEXT, "
                + Schedule.KEY_PREFERENCES + " TEXT )";

        db.execSQL(CREATE_TABLE_GAME);

        List<Schedule> mSchedules = LoadConfiguration.getSchedulesDummy();
        for(Schedule s : mSchedules) {
            ContentValues contentValues = s.getContentValues();
            db.insert(Schedule.TABLE, null, contentValues);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Schedule.TABLE);
        onCreate(db);
    }
}
