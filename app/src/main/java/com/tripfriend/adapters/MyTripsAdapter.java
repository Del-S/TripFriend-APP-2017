package com.tripfriend.adapters;

import android.content.ContentUris;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorTreeAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tripfriend.BaseActivity;
import com.tripfriend.R;
import com.tripfriend.activities.MyTripsActivity;
import com.tripfriend.activities.PlanTripActivity;
import com.tripfriend.adapters.wrapper.ScheduleCursorWrapper;
import com.tripfriend.model.Friend;
import com.tripfriend.model.Schedule;
import com.tripfriend.model.configuration.Configuration;
import com.tripfriend.model.database.ScheduleProvider;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;

public class MyTripsAdapter extends CursorTreeAdapter {

    private LayoutInflater mInflator;
    private ScheduleCursorWrapper groupCursor, childCursor;
    private MyTripsActivity mActivity;
    private Context context;
    private Configuration c = Configuration.getInstance();
    private SimpleDateFormat sdf = new SimpleDateFormat(BaseActivity.DATE_HOUR_FORMAT, BaseActivity.APP_LOCALE);

    public MyTripsAdapter(Cursor cursor, Context context) {
        super(cursor, context);
        this.context = context;
        mInflator = LayoutInflater.from(context);
        mActivity = (MyTripsActivity) context;
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        if (observer != null) {
            super.unregisterDataSetObserver(observer);
        }
    }

    @Override
    protected Cursor getChildrenCursor(Cursor cursor) {
        // Given the group, we return a cursor for all the children within that
        // group
        int groupId = groupCursor.getInt(groupCursor
                .getColumnIndex(Schedule.KEY_ID));

        Uri uri = Uri.parse(ScheduleProvider.CONTENT_URI + "/" + groupId);

        CursorLoader cursorLoader = new CursorLoader(mActivity, uri, null, null, null, null);
        Cursor childCursor = null;

        try {
            childCursor = cursorLoader.loadInBackground();
            childCursor.moveToFirst();
        } catch (Exception e) {
            Log.e("MyTripsAdapter", e.getMessage());
        }

        return childCursor;
    }

    @Override
    protected View newGroupView(Context context, Cursor cursor, boolean b, ViewGroup viewGroup) {
        View mView = mInflator.inflate(
                R.layout.element_row_trip, null);
        return mView;
    }

    @Override
    protected void bindGroupView(View view, Context context, Cursor cursor, boolean b) {
        groupCursor = new ScheduleCursorWrapper(cursor);

        final Schedule s = groupCursor.getSchedule();
        ImageView mIcon = (ImageView) view.findViewById(R.id.ert_icon);
        TextView mLocation = (TextView) view.findViewById(R.id.ert_city);
        TextView mDateTime = (TextView) view.findViewById(R.id.ert_date_time);

        int imageId = context.getResources().getIdentifier("friend_"+s.getId_friend(), "drawable", context.getPackageName());
        mIcon.setImageResource(imageId);
        String location = c.getLocations().get(s.getLocation());
        mLocation.setText(location);
        String dateTime = sdf.format( s.getCalendar_start().getTime() );
        mDateTime.setText(dateTime);

        ImageView iv = (ImageView) view.findViewById(R.id.ert_expand);
        if (b) {
            iv.setImageResource(R.drawable.ic_expand_less_24dp);
            view.setBackgroundColor( ContextCompat.getColor(context, R.color.colorAccent) );
            mLocation.setTextColor( ContextCompat.getColor(context, android.R.color.white) );
            mDateTime.setTextColor( ContextCompat.getColor(context, android.R.color.white) );
        } else {
            iv.setImageResource(R.drawable.ic_expand_more_24dp);
            view.setBackgroundColor( ContextCompat.getColor(context, R.color.colorMyTrips) );
            mLocation.setTextColor( ContextCompat.getColor(context, R.color.colorPrimaryDark) );
            mDateTime.setTextColor( ContextCompat.getColor(context, R.color.colorGray) );
        }
    }

    @Override
    protected View newChildView(Context context, Cursor cursor, boolean b, ViewGroup viewGroup) {
        View mView = mInflator.inflate(
                R.layout.element_detail_trip, null);
        return mView;
    }

    @Override
    protected void bindChildView(View view, Context context, Cursor cursor, boolean b) {
        childCursor = new ScheduleCursorWrapper(cursor);
        final Schedule s = childCursor.getSchedule();

        TextView mLocation = (TextView) view.findViewById(R.id.edt_location);
        TextView mLanguage = (TextView) view.findViewById(R.id.edt_language);
        TextView mTimeSpan = (TextView) view.findViewById(R.id.edt_timespan);

        TextView mDateTime = (TextView) view.findViewById(R.id.edt_date_time);
        TextView mPreferences = (TextView) view.findViewById(R.id.edt_preferences);

        ImageView iv = (ImageView) view.findViewById(R.id.edt_friend_image);

        TextView mFrendName = (TextView) view.findViewById(R.id.edt_friend_name);

        String location = c.getLocations().get(s.getLocation());
        mLocation.setText(location);
        String language = c.getLanguages().get(s.getLanguage());
        mLanguage.setText(language);
        String timeSpan = c.getTime_spans().get(s.getTime_span());
        mTimeSpan.setText(timeSpan);

        String dateTime = sdf.format(s.getCalendar_start().getTime());
        mDateTime.setText(dateTime);

        StringBuilder friendsList = new StringBuilder();
            if(s.getPreferences() != null) {
            for(String preference : s.getPreferences() ){
                friendsList.append(preference);
                friendsList.append(",");
            }
            friendsList.deleteCharAt(friendsList.length() - 1);
            mPreferences.setText(friendsList.toString());
        }

        Friend f = c.getFriendByID(s.getId_friend());
        mFrendName.setText(f.getName());

        int imageId = context.getResources().getIdentifier("friend_"+s.getId_friend(), "drawable", context.getPackageName());
        iv.setImageResource(imageId);

        Button mDelete = (Button) view.findViewById(R.id.edt_delete_trip);
        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteAllMessage(s.getId());
            }
        });

        Button mEdit = (Button) view.findViewById(R.id.edt_edit_trip);
        mEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Schedule.forcePlanInstance(s);
                Intent i = new Intent(mActivity, PlanTripActivity.class);
                mActivity.startActivity(i);
            }
        });
    }

    private void showDeleteAllMessage(final long id) {
        new AlertDialog.Builder(mActivity)
                .setTitle("Delete the tour")
                .setMessage("Are you sure you want to delete this tour?")
                .setIcon(R.drawable.ic_warning_black_24dp)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteTrip(id);
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    private void deleteTrip(long id) {
        // Defines selection criteria for the rows you want to delete
        String mSelectionClause = Schedule.KEY_ID + " LIKE ?";
        String[] mSelectionArgs = {Long.toString(id)};
        // Defines a variable to contain the number of rows deleted
        int mRowsDeleted = 0;

        mRowsDeleted = mActivity.getContentResolver().delete(
                ScheduleProvider.CONTENT_URI,
                mSelectionClause,
                mSelectionArgs
        );

        if(mRowsDeleted > 0) {
            CursorLoader cursorLoader = new CursorLoader(mActivity, ScheduleProvider.CONTENT_URI, null, null, null, null);
            this.changeCursor(cursorLoader.loadInBackground());
        }
    }

}
